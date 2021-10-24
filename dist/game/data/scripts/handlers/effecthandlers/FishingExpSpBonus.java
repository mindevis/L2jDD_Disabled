
package handlers.effecthandlers;

import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.stats.Stat;

/**
 * @author Mobius
 */
public class FishingExpSpBonus extends AbstractStatPercentEffect
{
	public FishingExpSpBonus(StatSet params)
	{
		super(params, Stat.FISHING_EXP_SP_BONUS);
	}
}
