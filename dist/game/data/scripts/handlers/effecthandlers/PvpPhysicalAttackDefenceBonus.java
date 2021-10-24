
package handlers.effecthandlers;

import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.stats.Stat;

/**
 * @author Sdw
 */
public class PvpPhysicalAttackDefenceBonus extends AbstractStatPercentEffect
{
	public PvpPhysicalAttackDefenceBonus(StatSet params)
	{
		super(params, Stat.PVP_PHYSICAL_ATTACK_DEFENCE);
	}
}
