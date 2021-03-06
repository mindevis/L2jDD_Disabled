
package handlers.targethandlers;

import org.l2jdd.gameserver.geoengine.GeoEngine;
import org.l2jdd.gameserver.handler.ITargetTypeHandler;
import org.l2jdd.gameserver.model.WorldObject;
import org.l2jdd.gameserver.model.actor.Creature;
import org.l2jdd.gameserver.model.skills.Skill;
import org.l2jdd.gameserver.model.skills.targets.TargetType;
import org.l2jdd.gameserver.network.SystemMessageId;

/**
 * Target only enemy no matter if force attacking or not.
 * @author Nik
 */
public class EnemyOnly implements ITargetTypeHandler
{
	@Override
	public Enum<TargetType> getTargetType()
	{
		return TargetType.ENEMY_ONLY;
	}
	
	@Override
	public WorldObject getTarget(Creature creature, WorldObject selectedTarget, Skill skill, boolean forceUse, boolean dontMove, boolean sendMessage)
	{
		if (selectedTarget == null)
		{
			return null;
		}
		
		if (!selectedTarget.isCreature())
		{
			return null;
		}
		
		final Creature target = (Creature) selectedTarget;
		
		// You cannot attack yourself even with force.
		if (creature == target)
		{
			if (sendMessage)
			{
				creature.sendPacket(SystemMessageId.INVALID_TARGET);
			}
			return null;
		}
		
		// You cannot attack dead targets.
		if (target.isDead() && !skill.isStayAfterDeath())
		{
			if (sendMessage)
			{
				creature.sendPacket(SystemMessageId.INVALID_TARGET);
			}
			return null;
		}
		
		// Doors do not care about force attack.
		if (target.isDoor() && !target.isAutoAttackable(creature))
		{
			if (sendMessage)
			{
				creature.sendPacket(SystemMessageId.INVALID_TARGET);
			}
			return null;
		}
		
		// Monsters can attack/be attacked anywhere. Players can attack creatures that aren't autoattackable with force attack.
		if (target.isAutoAttackable(creature))
		{
			// Check for cast range if character cannot move. TODO: char will start follow until within castrange, but if his moving is blocked by geodata, this msg will be sent.
			if (dontMove && (creature.calculateDistance2D(target) > skill.getCastRange()))
			{
				if (sendMessage)
				{
					creature.sendPacket(SystemMessageId.THE_DISTANCE_IS_TOO_FAR_AND_SO_THE_CASTING_HAS_BEEN_STOPPED);
				}
				return null;
			}
			
			// Geodata check when character is within range.
			if (!GeoEngine.getInstance().canSeeTarget(creature, target))
			{
				if (sendMessage)
				{
					creature.sendPacket(SystemMessageId.CANNOT_SEE_TARGET);
				}
				return null;
			}
			
			// Skills with this target type cannot be used by playables on playables in peace zone, but can be used by and on NPCs.
			if (target.isInsidePeaceZone(creature))
			{
				if (sendMessage)
				{
					creature.sendPacket(SystemMessageId.YOU_CANNOT_USE_SKILLS_THAT_MAY_HARM_OTHER_PLAYERS_IN_THIS_AREA);
				}
				return null;
			}
			
			// Is this check still actual?
			if ((target.getActingPlayer() != null) && (creature.getActingPlayer() != null) && creature.getActingPlayer().isSiegeFriend(target))
			{
				if (sendMessage)
				{
					creature.sendPacket(SystemMessageId.FORCE_ATTACK_IS_IMPOSSIBLE_AGAINST_A_TEMPORARY_ALLIED_MEMBER_DURING_A_SIEGE);
				}
				return null;
			}
			
			return target;
		}
		
		if (sendMessage)
		{
			creature.sendPacket(SystemMessageId.INVALID_TARGET);
		}
		
		return null;
	}
}
