
package handlers.skillconditionhandlers;

import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.WorldObject;
import org.l2jdd.gameserver.model.actor.Creature;
import org.l2jdd.gameserver.model.skills.ISkillCondition;
import org.l2jdd.gameserver.model.skills.Skill;

/**
 * @author Mobius
 */
public class OpAffectedBySkillSkillCondition implements ISkillCondition
{
	private final int _skillId;
	
	public OpAffectedBySkillSkillCondition(StatSet params)
	{
		_skillId = params.getInt("skillId", -1);
	}
	
	@Override
	public boolean canUse(Creature caster, Skill skill, WorldObject target)
	{
		return caster.getEffectList().getBuffInfoBySkillId(_skillId) != null;
	}
}
