
package handlers.effecthandlers;

import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.stats.Stat;

/**
 * @author Sdw
 */
public class LimitMp extends AbstractStatEffect
{
	public LimitMp(StatSet params)
	{
		super(params, Stat.MAX_RECOVERABLE_MP);
	}
}
