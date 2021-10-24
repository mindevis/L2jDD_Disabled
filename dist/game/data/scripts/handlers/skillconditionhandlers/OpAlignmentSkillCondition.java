
package handlers.skillconditionhandlers;

import org.l2jdd.gameserver.enums.SkillConditionAffectType;
import org.l2jdd.gameserver.enums.SkillConditionAlignment;
import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.WorldObject;
import org.l2jdd.gameserver.model.actor.Creature;
import org.l2jdd.gameserver.model.skills.ISkillCondition;
import org.l2jdd.gameserver.model.skills.Skill;

/**
 * @author Sdw
 */
public class OpAlignmentSkillCondition implements ISkillCondition
{
	private final SkillConditionAffectType _affectType;
	private final SkillConditionAlignment _alignment;
	
	public OpAlignmentSkillCondition(StatSet params)
	{
		_affectType = params.getEnum("affectType", SkillConditionAffectType.class);
		_alignment = params.getEnum("alignment", SkillConditionAlignment.class);
	}
	
	@Override
	public boolean canUse(Creature caster, Skill skill, WorldObject target)
	{
		switch (_affectType)
		{
			case CASTER:
			{
				return _alignment.test(caster.getActingPlayer());
			}
			case TARGET:
			{
				if ((target != null) && target.isPlayer())
				{
					return _alignment.test(target.getActingPlayer());
				}
				break;
			}
		}
		return false;
	}
}
