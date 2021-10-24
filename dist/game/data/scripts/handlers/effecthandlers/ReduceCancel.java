
package handlers.effecthandlers;

import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.stats.Stat;

/**
 * @author Sdw
 */
public class ReduceCancel extends AbstractStatEffect
{
	public ReduceCancel(StatSet params)
	{
		super(params, Stat.ATTACK_CANCEL);
	}
}
