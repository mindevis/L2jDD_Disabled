
package handlers.effecthandlers;

import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.stats.Stat;

/**
 * @author Mobius
 */
public class BonusSpoilRate extends AbstractStatPercentEffect
{
	public BonusSpoilRate(StatSet params)
	{
		super(params, Stat.BONUS_SPOIL_RATE);
	}
}
