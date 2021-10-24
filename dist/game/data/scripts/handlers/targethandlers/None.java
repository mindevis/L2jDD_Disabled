
package handlers.targethandlers;

import org.l2jdd.gameserver.handler.ITargetTypeHandler;
import org.l2jdd.gameserver.model.WorldObject;
import org.l2jdd.gameserver.model.actor.Creature;
import org.l2jdd.gameserver.model.skills.Skill;
import org.l2jdd.gameserver.model.skills.targets.TargetType;

/**
 * @author Sdw
 */
public class None implements ITargetTypeHandler
{
	@Override
	public WorldObject getTarget(Creature creature, WorldObject selectedTarget, Skill skill, boolean forceUse, boolean dontMove, boolean sendMessage)
	{
		return creature;
	}
	
	@Override
	public Enum<TargetType> getTargetType()
	{
		return TargetType.NONE;
	}
}
