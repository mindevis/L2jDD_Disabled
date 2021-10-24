
package handlers.effecthandlers;

import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.stats.Stat;

/**
 * @author Sdw
 */
public class SafeFallHeight extends AbstractStatEffect
{
	public SafeFallHeight(StatSet params)
	{
		super(params, Stat.FALL);
	}
}
