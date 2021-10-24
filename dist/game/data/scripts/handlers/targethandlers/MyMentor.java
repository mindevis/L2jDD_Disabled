
package handlers.targethandlers;

import org.l2jdd.gameserver.handler.ITargetTypeHandler;
import org.l2jdd.gameserver.instancemanager.MentorManager;
import org.l2jdd.gameserver.model.Mentee;
import org.l2jdd.gameserver.model.WorldObject;
import org.l2jdd.gameserver.model.actor.Creature;
import org.l2jdd.gameserver.model.skills.Skill;
import org.l2jdd.gameserver.model.skills.targets.TargetType;

/**
 * Target my mentor.
 * @author Nik
 */
public class MyMentor implements ITargetTypeHandler
{
	@Override
	public Enum<TargetType> getTargetType()
	{
		return TargetType.MY_MENTOR;
	}
	
	@Override
	public WorldObject getTarget(Creature creature, WorldObject selectedTarget, Skill skill, boolean forceUse, boolean dontMove, boolean sendMessage)
	{
		if (creature.isPlayer())
		{
			final Mentee mentor = MentorManager.getInstance().getMentor(creature.getObjectId());
			if (mentor != null)
			{
				return mentor.getPlayerInstance();
			}
		}
		return null;
	}
}
