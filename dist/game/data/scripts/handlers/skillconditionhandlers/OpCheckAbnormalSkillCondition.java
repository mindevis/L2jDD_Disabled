
package handlers.skillconditionhandlers;

import org.l2jdd.gameserver.enums.SkillConditionAffectType;
import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.WorldObject;
import org.l2jdd.gameserver.model.actor.Creature;
import org.l2jdd.gameserver.model.skills.AbnormalType;
import org.l2jdd.gameserver.model.skills.ISkillCondition;
import org.l2jdd.gameserver.model.skills.Skill;

/**
 * @author UnAfraid
 */
public class OpCheckAbnormalSkillCondition implements ISkillCondition
{
	private final AbnormalType _type;
	private final int _level;
	private final boolean _hasAbnormal;
	private final SkillConditionAffectType _affectType;
	
	public OpCheckAbnormalSkillCondition(StatSet params)
	{
		_type = params.getEnum("type", AbnormalType.class);
		_level = params.getInt("level");
		_hasAbnormal = params.getBoolean("hasAbnormal");
		_affectType = params.getEnum("affectType", SkillConditionAffectType.class, SkillConditionAffectType.TARGET);
	}
	
	@Override
	public boolean canUse(Creature caster, Skill skill, WorldObject target)
	{
		switch (_affectType)
		{
			case CASTER:
			{
				return caster.getEffectList().hasAbnormalType(_type, info -> (info.getSkill().getAbnormalLevel() >= _level)) == _hasAbnormal;
			}
			case TARGET:
			{
				if ((target != null) && target.isCreature())
				{
					return ((Creature) target).getEffectList().hasAbnormalType(_type, info -> (info.getSkill().getAbnormalLevel() >= _level)) == _hasAbnormal;
				}
			}
		}
		return false;
	}
}
