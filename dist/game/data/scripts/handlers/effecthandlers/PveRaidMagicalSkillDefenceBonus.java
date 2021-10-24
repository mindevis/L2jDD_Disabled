
package handlers.effecthandlers;

import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.stats.Stat;

/**
 * @author Sdw
 */
public class PveRaidMagicalSkillDefenceBonus extends AbstractStatPercentEffect
{
	public PveRaidMagicalSkillDefenceBonus(StatSet params)
	{
		super(params, Stat.PVE_RAID_MAGICAL_SKILL_DEFENCE);
	}
}
