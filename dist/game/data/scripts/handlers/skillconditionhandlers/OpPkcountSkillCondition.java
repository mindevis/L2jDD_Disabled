
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
public class OpPkcountSkillCondition implements ISkillCondition
{
	private final SkillConditionAffectType _affectType;
	
	public OpPkcountSkillCondition(StatSet params)
	{
		_affectType = params.getEnum("affectType", SkillConditionAffectType.class);
	}
	
	@Override
	public boolean canUse(Creature caster, Skill skill, WorldObject target)
	{
		switch (_affectType)
		{
			case CASTER:
			{
				return caster.isPlayer() && (caster.getActingPlayer().getPkKills() > 0);
			}
			case TARGET:
			{
				if ((target != null) && target.isPlayer())
				{
					return target.getActingPlayer().getPkKills() > 0;
				}
				break;
			}
		}
		return false;
	}
}
