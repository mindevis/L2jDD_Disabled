
package org.l2jdd.gameserver.model.actor.instance;

import org.l2jdd.gameserver.ai.CtrlIntention;
import org.l2jdd.gameserver.enums.InstanceType;
import org.l2jdd.gameserver.geoengine.GeoEngine;
import org.l2jdd.gameserver.instancemanager.CastleManager;
import org.l2jdd.gameserver.instancemanager.FortManager;
import org.l2jdd.gameserver.model.World;
import org.l2jdd.gameserver.model.actor.Attackable;
import org.l2jdd.gameserver.model.actor.Creature;
import org.l2jdd.gameserver.model.actor.templates.NpcTemplate;
import org.l2jdd.gameserver.model.siege.Castle;
import org.l2jdd.gameserver.model.siege.Fort;
import org.l2jdd.gameserver.model.skills.Skill;
import org.l2jdd.gameserver.network.serverpackets.ActionFailed;

public class DefenderInstance extends Attackable
{
	private Castle _castle = null; // the castle which the instance should defend
	private Fort _fort = null; // the fortress which the instance should defend
	
	public DefenderInstance(NpcTemplate template)
	{
		super(template);
		setInstanceType(InstanceType.DefenderInstance);
	}
	
	@Override
	public void addDamage(Creature attacker, int damage, Skill skill)
	{
		super.addDamage(attacker, damage, skill);
		World.getInstance().forEachVisibleObjectInRange(this, DefenderInstance.class, 500, defender -> defender.addDamageHate(attacker, 0, 10));
	}
	
	/**
	 * Return True if a siege is in progress and the Creature attacker isn't a Defender.
	 * @param attacker The Creature that the SiegeGuardInstance try to attack
	 */
	@Override
	public boolean isAutoAttackable(Creature attacker)
	{
		// Attackable during siege by all except defenders
		if (!attacker.isPlayable())
		{
			return false;
		}
		
		final PlayerInstance player = attacker.getActingPlayer();
		
		// Check if siege is in progress
		if (((_fort != null) && _fort.getZone().isActive()) || ((_castle != null) && _castle.getZone().isActive()))
		{
			final int activeSiegeId = (_fort != null) ? _fort.getResidenceId() : _castle.getResidenceId();
			
			// Check if player is an enemy of this defender npc
			if ((player != null) && (((player.getSiegeState() == 2) && !player.isRegisteredOnThisSiegeField(activeSiegeId)) || (player.getSiegeState() == 1) || (player.getSiegeState() == 0)))
			{
				return true;
			}
		}
		return false;
	}
	
	@Override
	public boolean hasRandomAnimation()
	{
		return false;
	}
	
	/**
	 * This method forces guard to return to home location previously set
	 */
	@Override
	public void returnHome()
	{
		if (getWalkSpeed() <= 0)
		{
			return;
		}
		if (getSpawn() == null)
		{
			return;
		}
		if (!isInsideRadius2D(getSpawn(), 40))
		{
			setReturningToSpawnPoint(true);
			clearAggroList();
			
			if (hasAI())
			{
				getAI().setIntention(CtrlIntention.AI_INTENTION_MOVE_TO, getSpawn().getLocation());
			}
		}
	}
	
	@Override
	public void onSpawn()
	{
		super.onSpawn();
		
		_fort = FortManager.getInstance().getFort(getX(), getY(), getZ());
		_castle = CastleManager.getInstance().getCastle(getX(), getY(), getZ());
		if ((_fort == null) && (_castle == null))
		{
			LOGGER.warning("DefenderInstance spawned outside of Fortress or Castle zone!" + this);
		}
	}
	
	/**
	 * Custom onAction behaviour. Note that super() is not called because guards need extra check to see if a player should interact or ATTACK them when clicked.
	 */
	@Override
	public void onAction(PlayerInstance player, boolean interact)
	{
		if (!canTarget(player))
		{
			player.sendPacket(ActionFailed.STATIC_PACKET);
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
			// this max heigth difference might need some tweaking
			if (isAutoAttackable(player) && !isAlikeDead() && (Math.abs(player.getZ() - getZ()) < 600))
			{
				player.getAI().setIntention(CtrlIntention.AI_INTENTION_ATTACK, this);
			}
			// Notify the PlayerInstance AI with AI_INTENTION_INTERACT
			if (!isAutoAttackable(player) && !canInteract(player))
			{
				player.getAI().setIntention(CtrlIntention.AI_INTENTION_INTERACT, this);
			}
		}
		// Send a Server->Client ActionFailed to the PlayerInstance in order to avoid that the client wait another packet
		player.sendPacket(ActionFailed.STATIC_PACKET);
	}
	
	@Override
	public void useMagic(Skill skill)
	{
		if (!skill.isBad())
		{
			Creature target = this;
			double lowestHpValue = Double.MAX_VALUE;
			for (Creature nearby : World.getInstance().getVisibleObjectsInRange(this, Creature.class, skill.getCastRange()))
			{
				if ((nearby == null) || nearby.isDead() || !GeoEngine.getInstance().canSeeTarget(this, nearby))
				{
					continue;
				}
				if (nearby instanceof DefenderInstance)
				{
					final double targetHp = nearby.getCurrentHp();
					if (lowestHpValue > targetHp)
					{
						target = nearby;
						lowestHpValue = targetHp;
					}
				}
				else if (nearby.isPlayer())
				{
					final PlayerInstance player = (PlayerInstance) nearby;
					if ((player.getSiegeState() == 2) && !player.isRegisteredOnThisSiegeField(getScriptValue()))
					{
						final double targetHp = nearby.getCurrentHp();
						if (lowestHpValue > targetHp)
						{
							target = nearby;
							lowestHpValue = targetHp;
						}
					}
				}
			}
			setTarget(target);
		}
		super.useMagic(skill);
	}
	
	@Override
	public void addDamageHate(Creature attacker, int damage, int aggro)
	{
		if (attacker == null)
		{
			return;
		}
		
		if (!(attacker instanceof DefenderInstance))
		{
			if ((damage == 0) && (aggro <= 1) && (attacker.isPlayable()))
			{
				final PlayerInstance player = attacker.getActingPlayer();
				// Check if siege is in progress
				if (((_fort != null) && _fort.getZone().isActive()) || ((_castle != null) && _castle.getZone().isActive()))
				{
					final int activeSiegeId = (_fort != null) ? _fort.getResidenceId() : _castle.getResidenceId();
					
					// Do not add hate on defenders.
					if ((player.getSiegeState() == 2) && player.isRegisteredOnThisSiegeField(activeSiegeId))
					{
						return;
					}
				}
			}
			super.addDamageHate(attacker, damage, aggro);
		}
	}
}
