
package handlers.effecthandlers;

import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.stats.Stat;

/**
 * @author Mobius
 */
public class AdditionalPotionMp extends AbstractStatAddEffect
{
	public AdditionalPotionMp(StatSet params)
	{
		super(params, Stat.ADDITIONAL_POTION_MP);
	}
}
