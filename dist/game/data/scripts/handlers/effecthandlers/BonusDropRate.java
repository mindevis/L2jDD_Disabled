
package handlers.effecthandlers;

import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.stats.Stat;

/**
 * @author Mobius
 */
public class BonusDropRate extends AbstractStatPercentEffect
{
	public BonusDropRate(StatSet params)
	{
		super(params, Stat.BONUS_DROP_RATE);
	}
}
