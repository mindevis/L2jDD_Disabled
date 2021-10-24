
package handlers.effecthandlers;

import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.stats.Stat;

/**
 * @author Sdw
 */
public class VitalityPointsRate extends AbstractStatPercentEffect
{
	public VitalityPointsRate(StatSet params)
	{
		super(params, Stat.VITALITY_CONSUME_RATE);
	}
}
