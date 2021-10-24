
package handlers.effecthandlers;

import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.stats.Stat;

/**
 * @author Sdw
 */
public class WeightPenalty extends AbstractStatAddEffect
{
	public WeightPenalty(StatSet params)
	{
		super(params, Stat.WEIGHT_PENALTY);
	}
}
