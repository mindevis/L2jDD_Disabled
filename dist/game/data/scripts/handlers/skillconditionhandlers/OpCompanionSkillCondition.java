
package handlers.skillconditionhandlers;

import org.l2jdd.gameserver.enums.SkillConditionCompanionType;
import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.WorldObject;
import org.l2jdd.gameserver.model.actor.Creature;
import org.l2jdd.gameserver.model.skills.ISkillCondition;
import org.l2jdd.gameserver.model.skills.Skill;

/**
 * @author Sdw
 */
public class OpCompanionSkillCondition implements ISkillCondition
{
	private final SkillConditionCompanionType _type;
	
	public OpCompanionSkillCondition(StatSet params)
	{
		_type = params.getEnum("type", SkillConditionCompanionType.class);
	}
	
	@Override
	public boolean canUse(Creature caster, Skill skill, WorldObject target)
	{
		if (target != null)
		{
			switch (_type)
			{
				case PET:
				{
					return target.isPet();
				}
				case MY_SUMMON:
				{
					return target.isSummon() && (caster.getServitor(target.getObjectId()) != null);
				}
			}
		}
		return false;
	}
}
