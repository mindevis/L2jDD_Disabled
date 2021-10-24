
package handlers.effecthandlers;

import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.stats.Stat;

/**
 * @author Sdw
 */
public class PveRaidPhysicalAttackDefenceBonus extends AbstractStatPercentEffect
{
	public PveRaidPhysicalAttackDefenceBonus(StatSet params)
	{
		super(params, Stat.PVE_RAID_PHYSICAL_ATTACK_DEFENCE);
	}
}
