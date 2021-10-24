
package handlers.effecthandlers;

import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.stats.Stat;

/**
 * @author zarco
 */
public class EnchantRate extends AbstractStatAddEffect
{
	public EnchantRate(StatSet params)
	{
		super(params, Stat.ENCHANT_RATE);
	}
}
