
package handlers.effecthandlers;

import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.stats.Stat;

/**
 * @author Sdw
 */
public class DefenceCriticalRate extends AbstractStatEffect
{
	public DefenceCriticalRate(StatSet params)
	{
		super(params, Stat.DEFENCE_CRITICAL_RATE, Stat.DEFENCE_CRITICAL_RATE_ADD);
	}
}
