
package handlers.skillconditionhandlers;

import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.WorldObject;
import org.l2jdd.gameserver.model.actor.Creature;
import org.l2jdd.gameserver.model.skills.ISkillCondition;
import org.l2jdd.gameserver.model.skills.Skill;

public class CanAddMaxEntranceInzoneSkillCondition implements ISkillCondition
{
	public CanAddMaxEntranceInzoneSkillCondition(StatSet params)
	{
	}
	
	@Override
	public boolean canUse(Creature caster, Skill skill, WorldObject target)
	{
		return true; // TODO
	}
}
