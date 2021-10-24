
package handlers.effecthandlers;

import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.stats.Stat;

/**
 * @author Sdw
 */
public class SkillCriticalDamage extends AbstractStatEffect
{
	public SkillCriticalDamage(StatSet params)
	{
		super(params, Stat.CRITICAL_DAMAGE_SKILL, Stat.CRITICAL_DAMAGE_SKILL_ADD);
	}
}
