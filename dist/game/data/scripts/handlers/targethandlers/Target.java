
package handlers.targethandlers;

import org.l2jdd.gameserver.geoengine.GeoEngine;
import org.l2jdd.gameserver.handler.ITargetTypeHandler;
import org.l2jdd.gameserver.model.WorldObject;
import org.l2jdd.gameserver.model.actor.Creature;
import org.l2jdd.gameserver.model.skills.Skill;
import org.l2jdd.gameserver.model.skills.targets.TargetType;
import org.l2jdd.gameserver.network.SystemMessageId;

/**
 * Any friendly selected target or enemy if force use. Works on dead targets or doors as well.
 * @author Nik
 */
public class Target implements ITargetTypeHandler
{
	@Override
	public Enum<TargetType> getTargetType()
	{
		return TargetType.TARGET;
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
			if (sendMessage)
			{
				creature.sendPacket(SystemMessageId.INVALID_TARGET);
			}
			return null;
		}
		
		final Creature target = (Creature) selectedTarget;
		
		// You can always target yourself.
		if (creature == target)
		{
			return target;
		}
		
		// Check for cast range if character cannot move. TODO: char will start follow until within castrange, but if his moving is blocked by geodata, this msg will be sent.
		if (dontMove && (creature.calculateDistance2D(target) > skill.getCastRange()))
		{
			if (sendMessage)
			{
				creature.sendPacket(SystemMessageId.THE_DISTANCE_IS_TOO_FAR_AND_SO_THE_CASTING_HAS_BEEN_STOPPED);
			}
			
			return null;
		}
		
		if (skill.isFlyType() && !GeoEngine.getInstance().canMoveToTarget(creature.getX(), creature.getY(), creature.getZ(), target.getX(), target.getY(), target.getZ(), creature.getInstanceWorld()))
		{
			if (sendMessage)
			{
				creature.sendPacket(SystemMessageId.THE_TARGET_IS_LOCATED_WHERE_YOU_CANNOT_CHARGE);
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
		return target;
	}
}
