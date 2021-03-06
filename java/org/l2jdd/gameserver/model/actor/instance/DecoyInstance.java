
package org.l2jdd.gameserver.model.actor.instance;

import java.util.concurrent.Future;
import java.util.logging.Level;

import org.l2jdd.commons.concurrent.ThreadPool;
import org.l2jdd.gameserver.data.xml.SkillData;
import org.l2jdd.gameserver.enums.InstanceType;
import org.l2jdd.gameserver.instancemanager.ZoneManager;
import org.l2jdd.gameserver.model.PlayerCondOverride;
import org.l2jdd.gameserver.model.World;
import org.l2jdd.gameserver.model.actor.Creature;
import org.l2jdd.gameserver.model.actor.templates.NpcTemplate;
import org.l2jdd.gameserver.model.items.Weapon;
import org.l2jdd.gameserver.model.items.instance.ItemInstance;
import org.l2jdd.gameserver.model.skills.Skill;
import org.l2jdd.gameserver.network.SystemMessageId;
import org.l2jdd.gameserver.network.serverpackets.CharInfo;
import org.l2jdd.gameserver.network.serverpackets.IClientOutgoingPacket;
import org.l2jdd.gameserver.taskmanager.DecayTaskManager;

public class DecoyInstance extends Creature
{
	private final PlayerInstance _owner;
	private int _totalLifeTime;
	private int _timeRemaining;
	private Future<?> _DecoyLifeTask;
	private Future<?> _HateSpam;
	
	public DecoyInstance(NpcTemplate template, PlayerInstance owner, int totalLifeTime)
	{
		super(template);
		setInstanceType(InstanceType.DecoyInstance);
		_owner = owner;
		setXYZInvisible(owner.getX(), owner.getY(), owner.getZ());
		setInvul(false);
		_totalLifeTime = totalLifeTime;
		_timeRemaining = _totalLifeTime;
		final int hateSpamSkillId = 5272;
		final int skilllevel = Math.min(getTemplate().getDisplayId() - 13070, SkillData.getInstance().getMaxLevel(hateSpamSkillId));
		_DecoyLifeTask = ThreadPool.scheduleAtFixedRate(new DecoyLifetime(_owner, this), 1000, 1000);
		_HateSpam = ThreadPool.scheduleAtFixedRate(new HateSpam(this, SkillData.getInstance().getSkill(hateSpamSkillId, skilllevel)), 2000, 5000);
	}
	
	@Override
	public boolean doDie(Creature killer)
	{
		if (!super.doDie(killer))
		{
			return false;
		}
		if (_HateSpam != null)
		{
			_HateSpam.cancel(true);
			_HateSpam = null;
		}
		_totalLifeTime = 0;
		DecayTaskManager.getInstance().add(this);
		return true;
	}
	
	static class DecoyLifetime implements Runnable
	{
		private final PlayerInstance _player;
		
		private final DecoyInstance _Decoy;
		
		DecoyLifetime(PlayerInstance player, DecoyInstance decoy)
		{
			_player = player;
			_Decoy = decoy;
		}
		
		@Override
		public void run()
		{
			try
			{
				_Decoy.decTimeRemaining(1000);
				final double newTimeRemaining = _Decoy.getTimeRemaining();
				if (newTimeRemaining < 0)
				{
					_Decoy.unSummon(_player);
				}
			}
			catch (Exception e)
			{
				LOGGER.log(Level.SEVERE, "Decoy Error: ", e);
			}
		}
	}
	
	private static class HateSpam implements Runnable
	{
		private final DecoyInstance _player;
		private final Skill _skill;
		
		HateSpam(DecoyInstance player, Skill hate)
		{
			_player = player;
			_skill = hate;
		}
		
		@Override
		public void run()
		{
			try
			{
				_player.setTarget(_player);
				_player.doCast(_skill);
			}
			catch (Throwable e)
			{
				LOGGER.log(Level.SEVERE, "Decoy Error: ", e);
			}
		}
	}
	
	public void unSummon(PlayerInstance owner)
	{
		if (_DecoyLifeTask != null)
		{
			_DecoyLifeTask.cancel(true);
			_DecoyLifeTask = null;
		}
		if (_HateSpam != null)
		{
			_HateSpam.cancel(true);
			_HateSpam = null;
		}
		
		if (isSpawned() && !isDead())
		{
			ZoneManager.getInstance().getRegion(this).removeFromZones(this);
			decayMe();
		}
	}
	
	public void decTimeRemaining(int value)
	{
		_timeRemaining -= value;
	}
	
	public int getTimeRemaining()
	{
		return _timeRemaining;
	}
	
	public int getTotalLifeTime()
	{
		return _totalLifeTime;
	}
	
	@Override
	public void onSpawn()
	{
		super.onSpawn();
		sendPacket(new CharInfo(this, false));
	}
	
	@Override
	public void updateAbnormalVisualEffects()
	{
		World.getInstance().forEachVisibleObject(this, PlayerInstance.class, player ->
		{
			if (isVisibleFor(player))
			{
				player.sendPacket(new CharInfo(this, isInvisible() && player.canOverrideCond(PlayerCondOverride.SEE_ALL_PLAYERS)));
			}
		});
	}
	
	public void stopDecay()
	{
		DecayTaskManager.getInstance().cancel(this);
	}
	
	@Override
	public void onDecay()
	{
		deleteMe(_owner);
	}
	
	@Override
	public boolean isAutoAttackable(Creature attacker)
	{
		return _owner.isAutoAttackable(attacker);
	}
	
	@Override
	public ItemInstance getActiveWeaponInstance()
	{
		return null;
	}
	
	@Override
	public Weapon getActiveWeaponItem()
	{
		return null;
	}
	
	@Override
	public ItemInstance getSecondaryWeaponInstance()
	{
		return null;
	}
	
	@Override
	public Weapon getSecondaryWeaponItem()
	{
		return null;
	}
	
	@Override
	public int getId()
	{
		return getTemplate().getId();
	}
	
	@Override
	public int getLevel()
	{
		return getTemplate().getLevel();
	}
	
	public void deleteMe(PlayerInstance owner)
	{
		decayMe();
	}
	
	public PlayerInstance getOwner()
	{
		return _owner;
	}
	
	@Override
	public PlayerInstance getActingPlayer()
	{
		return _owner;
	}
	
	@Override
	public NpcTemplate getTemplate()
	{
		return (NpcTemplate) super.getTemplate();
	}
	
	@Override
	public void sendInfo(PlayerInstance player)
	{
		player.sendPacket(new CharInfo(this, isInvisible() && player.canOverrideCond(PlayerCondOverride.SEE_ALL_PLAYERS)));
	}
	
	@Override
	public void sendPacket(IClientOutgoingPacket... packets)
	{
		if (_owner != null)
		{
			_owner.sendPacket(packets);
		}
	}
	
	@Override
	public void sendPacket(SystemMessageId id)
	{
		if (_owner != null)
		{
			_owner.sendPacket(id);
		}
	}
}
