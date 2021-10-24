
package handlers.effecthandlers;

import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.stats.Stat;

/**
 * @author Sdw
 */
public class PvePhysicalSkillDefenceBonus extends AbstractStatPercentEffect
{
	public PvePhysicalSkillDefenceBonus(StatSet params)
	{
		super(params, Stat.PVE_PHYSICAL_SKILL_DEFENCE);
	}
}
