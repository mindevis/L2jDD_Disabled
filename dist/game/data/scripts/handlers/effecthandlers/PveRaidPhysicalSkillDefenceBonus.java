
package handlers.effecthandlers;

import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.stats.Stat;

/**
 * @author Sdw
 */
public class PveRaidPhysicalSkillDefenceBonus extends AbstractStatPercentEffect
{
	public PveRaidPhysicalSkillDefenceBonus(StatSet params)
	{
		super(params, Stat.PVE_RAID_PHYSICAL_SKILL_DEFENCE);
	}
}
