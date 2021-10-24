
package handlers.effecthandlers;

import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.stats.Stat;

/**
 * @author Sdw
 */
public class PveMagicalSkillDefenceBonus extends AbstractStatPercentEffect
{
	public PveMagicalSkillDefenceBonus(StatSet params)
	{
		super(params, Stat.PVE_MAGICAL_SKILL_DEFENCE);
	}
}
