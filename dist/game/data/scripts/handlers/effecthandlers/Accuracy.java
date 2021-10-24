
package handlers.effecthandlers;

import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.stats.Stat;

/**
 * @author Sdw
 */
public class Accuracy extends AbstractStatEffect
{
	public Accuracy(StatSet params)
	{
		super(params, Stat.ACCURACY_COMBAT);
	}
}
