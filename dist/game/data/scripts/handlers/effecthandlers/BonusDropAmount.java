
package handlers.effecthandlers;

import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.stats.Stat;

/**
 * @author Mobius
 */
public class BonusDropAmount extends AbstractStatPercentEffect
{
	public BonusDropAmount(StatSet params)
	{
		super(params, Stat.BONUS_DROP_AMOUNT);
	}
}
