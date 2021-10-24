
package handlers.skillconditionhandlers;

import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.WorldObject;
import org.l2jdd.gameserver.model.actor.Creature;
import org.l2jdd.gameserver.model.skills.ISkillCondition;
import org.l2jdd.gameserver.model.skills.Skill;

/**
 * @author Mobius
 */
public class MaxMpSkillCondition implements ISkillCondition
{
	private final int _amount;
	
	public MaxMpSkillCondition(StatSet params)
	{
		_amount = params.getInt("amount");
	}
	
	@Override
	public boolean canUse(Creature caster, Skill skill, WorldObject target)
	{
		return caster.getMaxMp() >= _amount;
	}
}
