
package handlers.skillconditionhandlers;

import org.l2jdd.gameserver.enums.SkillConditionAffectType;
import org.l2jdd.gameserver.enums.SkillConditionPercentType;
import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.WorldObject;
import org.l2jdd.gameserver.model.actor.Creature;
import org.l2jdd.gameserver.model.skills.ISkillCondition;
import org.l2jdd.gameserver.model.skills.Skill;

/**
 * @author UnAfraid
 */
public class RemainHpPerSkillCondition implements ISkillCondition
{
	private final int _amount;
	private final SkillConditionPercentType _percentType;
	private final SkillConditionAffectType _affectType;
	
	public RemainHpPerSkillCondition(StatSet params)
	{
		_amount = params.getInt("amount");
		_percentType = params.getEnum("percentType", SkillConditionPercentType.class);
		_affectType = params.getEnum("affectType", SkillConditionAffectType.class);
	}
	
	@Override
	public boolean canUse(Creature caster, Skill skill, WorldObject target)
	{
		switch (_affectType)
		{
			case CASTER:
			{
				return _percentType.test(caster.getCurrentHpPercent(), _amount);
			}
			case TARGET:
			{
				if ((target != null) && target.isCreature())
				{
					return _percentType.test(((Creature) target).getCurrentHpPercent(), _amount);
				}
				break;
			}
		}
		return false;
	}
}
