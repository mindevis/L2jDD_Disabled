
package handlers.effecthandlers;

import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.stats.Stat;

/**
 * @author Sdw
 */
public class DefenceMagicCriticalRate extends AbstractStatEffect
{
	public DefenceMagicCriticalRate(StatSet params)
	{
		super(params, Stat.DEFENCE_MAGIC_CRITICAL_RATE, Stat.DEFENCE_MAGIC_CRITICAL_RATE_ADD);
	}
}
