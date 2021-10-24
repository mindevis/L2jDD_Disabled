
package handlers.effecthandlers;

import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.stats.Stat;

/**
 * @author Sdw
 */
public class MagicCriticalRate extends AbstractStatEffect
{
	public MagicCriticalRate(StatSet params)
	{
		super(params, Stat.MAGIC_CRITICAL_RATE);
	}
}
