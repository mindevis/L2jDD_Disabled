
package handlers.skillconditionhandlers;

import org.l2jdd.gameserver.instancemanager.MentorManager;
import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.WorldObject;
import org.l2jdd.gameserver.model.actor.Creature;
import org.l2jdd.gameserver.model.skills.ISkillCondition;
import org.l2jdd.gameserver.model.skills.Skill;

/**
 * @author UnAfraid
 */
public class TargetMyMenteeSkillCondition implements ISkillCondition
{
	public TargetMyMenteeSkillCondition(StatSet params)
	{
	}
	
	@Override
	public boolean canUse(Creature caster, Skill skill, WorldObject target)
	{
		if ((target == null) || !target.isPlayer())
		{
			return false;
		}
		return MentorManager.getInstance().getMentee(caster.getObjectId(), target.getObjectId()) != null;
	}
}
