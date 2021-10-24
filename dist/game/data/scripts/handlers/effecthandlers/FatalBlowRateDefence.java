
package handlers.effecthandlers;

import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.stats.Stat;

/**
 * @author Sahar
 */
public class FatalBlowRateDefence extends AbstractStatEffect
{
	public FatalBlowRateDefence(StatSet params)
	{
		super(params, Stat.BLOW_RATE_DEFENCE);
	}
}