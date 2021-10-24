
package handlers.effecthandlers;

import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.stats.Stat;

/**
 * @author Sdw
 */
public class PvePhysicalAttackDefenceBonus extends AbstractStatPercentEffect
{
	public PvePhysicalAttackDefenceBonus(StatSet params)
	{
		super(params, Stat.PVE_PHYSICAL_ATTACK_DEFENCE);
	}
}
