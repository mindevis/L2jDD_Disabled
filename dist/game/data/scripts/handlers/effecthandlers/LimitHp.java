
package handlers.effecthandlers;

import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.stats.Stat;

/**
 * @author Sdw
 */
public class LimitHp extends AbstractStatEffect
{
	public LimitHp(StatSet params)
	{
		super(params, Stat.MAX_RECOVERABLE_HP);
	}
}
