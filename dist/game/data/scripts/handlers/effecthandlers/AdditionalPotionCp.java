
package handlers.effecthandlers;

import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.stats.Stat;

/**
 * @author Mobius
 */
public class AdditionalPotionCp extends AbstractStatAddEffect
{
	public AdditionalPotionCp(StatSet params)
	{
		super(params, Stat.ADDITIONAL_POTION_CP);
	}
}
