
package handlers.skillconditionhandlers;

import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.WorldObject;
import org.l2jdd.gameserver.model.actor.Creature;
import org.l2jdd.gameserver.model.skills.ISkillCondition;
import org.l2jdd.gameserver.model.skills.Skill;
import org.l2jdd.gameserver.model.stats.Stat;

/**
 * @author UnAfraid
 */
public class OpSoulMaxSkillCondition implements ISkillCondition
{
	public OpSoulMaxSkillCondition(StatSet params)
	{
	}
	
	@Override
	public boolean canUse(Creature caster, Skill skill, WorldObject target)
	{
		final int maxSouls = (int) caster.getStat().getValue(Stat.MAX_SOULS);
		return caster.isPlayable() && (caster.getActingPlayer().getChargedSouls() < maxSouls);
	}
}
