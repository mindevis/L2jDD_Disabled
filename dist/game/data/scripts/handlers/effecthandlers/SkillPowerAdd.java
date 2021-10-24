
package handlers.effecthandlers;

import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.stats.Stat;

/**
 * @author Mobius
 */
public class SkillPowerAdd extends AbstractStatAddEffect
{
	public SkillPowerAdd(StatSet params)
	{
		super(params, Stat.SKILL_POWER_ADD);
	}
}
