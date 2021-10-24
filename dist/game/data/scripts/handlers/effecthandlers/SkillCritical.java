
package handlers.effecthandlers;

import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.actor.Creature;
import org.l2jdd.gameserver.model.effects.AbstractEffect;
import org.l2jdd.gameserver.model.skills.Skill;
import org.l2jdd.gameserver.model.stats.BaseStat;
import org.l2jdd.gameserver.model.stats.Stat;

/**
 * @author Sdw
 */
public class SkillCritical extends AbstractEffect
{
	private final BaseStat _stat;
	
	public SkillCritical(StatSet params)
	{
		_stat = params.getEnum("stat", BaseStat.class, BaseStat.STR);
	}
	
	@Override
	public void pump(Creature effected, Skill skill)
	{
		effected.getStat().mergeAdd(Stat.SKILL_CRITICAL, _stat.ordinal());
	}
}
