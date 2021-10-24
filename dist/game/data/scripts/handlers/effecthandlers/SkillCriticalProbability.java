
package handlers.effecthandlers;

import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.stats.Stat;

/**
 * @author Sdw
 */
public class SkillCriticalProbability extends AbstractStatPercentEffect
{
	public SkillCriticalProbability(StatSet params)
	{
		super(params, Stat.SKILL_CRITICAL_PROBABILITY);
	}
}
