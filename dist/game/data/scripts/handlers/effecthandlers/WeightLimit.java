
package handlers.effecthandlers;

import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.stats.Stat;

/**
 * @author Sdw
 */
public class WeightLimit extends AbstractStatEffect
{
	public WeightLimit(StatSet params)
	{
		super(params, Stat.WEIGHT_LIMIT);
	}
}
