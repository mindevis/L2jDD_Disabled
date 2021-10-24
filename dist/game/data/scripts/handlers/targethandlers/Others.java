
package handlers.targethandlers;

import org.l2jdd.gameserver.handler.ITargetTypeHandler;
import org.l2jdd.gameserver.model.WorldObject;
import org.l2jdd.gameserver.model.actor.Creature;
import org.l2jdd.gameserver.model.skills.Skill;
import org.l2jdd.gameserver.model.skills.targets.TargetType;
import org.l2jdd.gameserver.network.SystemMessageId;

/**
 * Target other things (skills with this target type appear to be disabled).
 * @author Nik
 */
public class Others implements ITargetTypeHandler
{
	@Override
	public Enum<TargetType> getTargetType()
	{
		return TargetType.OTHERS;
	}
	
	@Override
	public WorldObject getTarget(Creature creature, WorldObject selectedTarget, Skill skill, boolean forceUse, boolean dontMove, boolean sendMessage)
	{
		if (selectedTarget == creature)
		{
			creature.sendPacket(SystemMessageId.YOU_CANNOT_USE_THIS_ON_YOURSELF);
			return null;
		}
		return selectedTarget;
	}
}
