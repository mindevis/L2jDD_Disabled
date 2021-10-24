
package handlers.effecthandlers;

import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.stats.Stat;

/**
 * @author Sdw
 */
public class PvpPhysicalSkillDefenceBonus extends AbstractStatPercentEffect
{
	public PvpPhysicalSkillDefenceBonus(StatSet params)
	{
		super(params, Stat.PVP_PHYSICAL_SKILL_DEFENCE);
	}
}
