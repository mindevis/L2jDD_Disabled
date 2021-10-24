
package handlers.effecthandlers;

import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.stats.Stat;

/**
 * @author Sdw
 */
public class PvpPhysicalAttackDamageBonus extends AbstractStatPercentEffect
{
	public PvpPhysicalAttackDamageBonus(StatSet params)
	{
		super(params, Stat.PVP_PHYSICAL_ATTACK_DAMAGE);
	}
}
