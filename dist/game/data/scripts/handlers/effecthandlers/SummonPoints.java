
package handlers.effecthandlers;

import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.stats.Stat;

/**
 * @author Sdw
 */
public class SummonPoints extends AbstractStatAddEffect
{
	public SummonPoints(StatSet params)
	{
		super(params, Stat.MAX_SUMMON_POINTS);
	}
}
