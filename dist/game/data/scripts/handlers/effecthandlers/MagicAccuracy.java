
package handlers.effecthandlers;

import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.stats.Stat;

/**
 * @author Sdw
 */
public class MagicAccuracy extends AbstractStatEffect
{
	public MagicAccuracy(StatSet params)
	{
		super(params, Stat.ACCURACY_MAGIC);
	}
}
