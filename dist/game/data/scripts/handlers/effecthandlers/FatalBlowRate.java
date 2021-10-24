
package handlers.effecthandlers;

import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.stats.Stat;

/**
 * @author Sdw
 */
public class FatalBlowRate extends AbstractStatEffect
{
	public FatalBlowRate(StatSet params)
	{
		super(params, Stat.BLOW_RATE);
	}
}