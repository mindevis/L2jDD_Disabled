
package handlers.effecthandlers;

import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.stats.Stat;

/**
 * @author Sdw
 */
public class LimitCp extends AbstractStatEffect
{
	public LimitCp(StatSet params)
	{
		super(params, Stat.MAX_RECOVERABLE_CP);
	}
}
