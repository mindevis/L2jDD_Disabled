
package org.l2jdd.gameserver.model.actor.instance;

import org.l2jdd.commons.concurrent.ThreadPool;
import org.l2jdd.gameserver.ai.CtrlIntention;
import org.l2jdd.gameserver.enums.InstanceType;
import org.l2jdd.gameserver.instancemanager.FortSiegeManager;
import org.l2jdd.gameserver.instancemanager.SiegeManager;
import org.l2jdd.gameserver.model.SiegeClan;
import org.l2jdd.gameserver.model.actor.Creature;
import org.l2jdd.gameserver.model.actor.Npc;
import org.l2jdd.gameserver.model.actor.status.SiegeFlagStatus;
import org.l2jdd.gameserver.model.actor.templates.NpcTemplate;
import org.l2jdd.gameserver.model.clan.Clan;
import org.l2jdd.gameserver.model.siege.Siegable;
import org.l2jdd.gameserver.model.skills.Skill;
import org.l2jdd.gameserver.network.SystemMessageId;
import org.l2jdd.gameserver.network.serverpackets.ActionFailed;
import org.l2jdd.gameserver.network.serverpackets.SystemMessage;

public class SiegeFlagInstance extends Npc
{
	private final Clan _clan;
	private Siegable _siege;
	private final boolean _isAdvanced;
	private boolean _canTalk;
	
	public SiegeFlagInstance(PlayerInstance player, NpcTemplate template, boolean advanced)
	{
		super(template);
		setInstanceType(InstanceType.SiegeFlagInstance);
		
		_clan = player.getClan();
		_canTalk = true;
		_siege = SiegeManager.getInstance().getSiege(player.getX(), player.getY(), player.getZ());
		if (_siege == null)
		{
			_siege = FortSiegeManager.getInstance().getSiege(player.getX(), player.getY(), player.getZ());
		}
		if ((_clan == null) || (_siege == null))
		{
			throw new NullPointerException(getClass().getSimpleName() + ": Initialization failed.");
		}
		
		final SiegeClan sc = _siege.getAttackerClan(_clan);
		if (sc == null)
		{
			throw new NullPointerException(getClass().getSimpleName() + ": Cannot find siege clan.");
		}
		
		sc.addFlag(this);
		_isAdvanced = advanced;
		getStatus();
		setInvul(false);
	}
	
	@Override
	public boolean canBeAttacked()
	{
		return !isInvul();
	}
	
	@Override
	public boolean isAutoAttackable(Creature attacker)
	{
		return !isInvul();
	}
	
	@Override
	public boolean doDie(Creature killer)
	{
		if (!super.doDie(killer))
		{
			return false;
		}
		if ((_siege != null) && (_clan != null))
		{
			final SiegeClan sc = _siege.getAttackerClan(_clan);
			if (sc != null)
			{
				sc.removeFlag(this);
			}
		}
		return true;
	}
	
	@Override
	public void onForcedAttack(PlayerInstance player)
	{
		onAction(player);
	}
	
	@Override
	public void onAction(PlayerInstance player, boolean interact)
	{
		if ((player == null) || !canTarget(player))
		{
			return;
		}
		
		// Check if the PlayerInstance already target the NpcInstance
		if (this != player.getTarget())
		{
			// Set the target of the PlayerInstance player
			player.setTarget(this);
		}
		else if (interact)
		{
			if (isAutoAttackable(player) && (Math.abs(player.getZ() - getZ()) < 100))
			{
				player.getAI().setIntention(CtrlIntention.AI_INTENTION_ATTACK, this);
			}
			else
			{
				// Send a Server->Client ActionFailed to the PlayerInstance in order to avoid that the client wait another packet
				player.sendPacket(ActionFailed.STATIC_PACKET);
			}
		}
	}
	
	public boolean isAdvancedHeadquarter()
	{
		return _isAdvanced;
	}
	
	@Override
	public SiegeFlagStatus getStatus()
	{
		return (SiegeFlagStatus) super.getStatus();
	}
	
	@Override
	public void initCharStatus()
	{
		setStatus(new SiegeFlagStatus(this));
	}
	
	@Override
	public void reduceCurrentHp(double damage, Creature attacker, Skill skill)
	{
		super.reduceCurrentHp(damage, attacker, skill);
		if (canTalk() && (((getCastle() != null) && getCastle().getSiege().isInProgress()) || ((getFort() != null) && getFort().getSiege().isInProgress())) && (_clan != null))
		{
			// send warning to owners of headquarters that theirs base is under attack
			_clan.broadcastToOnlineMembers(new SystemMessage(SystemMessageId.YOUR_BASE_IS_BEING_ATTACKED));
			setCanTalk(false);
			ThreadPool.schedule(new ScheduleTalkTask(), 20000);
		}
	}
	
	private class ScheduleTalkTask implements Runnable
	{
		public ScheduleTalkTask()
		{
		}
		
		@Override
		public void run()
		{
			setCanTalk(true);
		}
	}
	
	void setCanTalk(boolean value)
	{
		_canTalk = value;
	}
	
	private boolean canTalk()
	{
		return _canTalk;
	}
}
