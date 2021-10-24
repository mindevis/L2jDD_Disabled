
package handlers.skillconditionhandlers;

import org.l2jdd.gameserver.enums.SkillConditionAffectType;
import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.WorldObject;
import org.l2jdd.gameserver.model.actor.Creature;
import org.l2jdd.gameserver.model.skills.ISkillCondition;
import org.l2jdd.gameserver.model.skills.Skill;

/**
 * @author UnAfraid
 */
public class CheckLevelSkillCondition implements ISkillCondition
{
	private final int _minLevel;
	private final int _maxLevel;
	private final SkillConditionAffectType _affectType;
	
	public CheckLevelSkillCondition(StatSet params)
	{
		_minLevel = params.getInt("minLevel", 1);
		_maxLevel = params.getInt("maxLevel", Integer.MAX_VALUE);
		_affectType = params.getEnum("affectType", SkillConditionAffectType.class, SkillConditionAffectType.CASTER);
	}
	
	@Override
	public boolean canUse(Creature caster, Skill skill, WorldObject target)
	{
		switch (_affectType)
		{
			case CASTER:
			{
				return (caster.getLevel() >= _minLevel) && (caster.getLevel() <= _maxLevel);
			}
			case TARGET:
			{
				if ((target != null) && target.isPlayer())
				{
					return (target.getActingPlayer().getLevel() >= _minLevel) && (target.getActingPlayer().getLevel() <= _maxLevel);
				}
				break;
			}
		}
		return false;
	}
}
