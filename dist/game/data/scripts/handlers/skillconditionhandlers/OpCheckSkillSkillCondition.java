
package handlers.skillconditionhandlers;

import org.l2jdd.gameserver.enums.SkillConditionAffectType;
import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.WorldObject;
import org.l2jdd.gameserver.model.actor.Creature;
import org.l2jdd.gameserver.model.skills.ISkillCondition;
import org.l2jdd.gameserver.model.skills.Skill;

/**
 * @author Sdw
 */
public class OpCheckSkillSkillCondition implements ISkillCondition
{
	private final int _skillId;
	private final SkillConditionAffectType _affectType;
	
	public OpCheckSkillSkillCondition(StatSet params)
	{
		_skillId = params.getInt("skillId");
		_affectType = params.getEnum("affectType", SkillConditionAffectType.class);
	}
	
	@Override
	public boolean canUse(Creature caster, Skill skill, WorldObject target)
	{
		switch (_affectType)
		{
			case CASTER:
			{
				return caster.getSkillLevel(_skillId) > 0;
			}
			case TARGET:
			{
				if ((target != null) && !target.isPlayer())
				{
					return target.getActingPlayer().getSkillLevel(_skillId) > 0;
				}
				break;
			}
		}
		return false;
	}
}
