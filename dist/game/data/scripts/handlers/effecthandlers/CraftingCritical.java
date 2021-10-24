
package handlers.effecthandlers;

import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.stats.Stat;

/**
 * @author Nik
 */
public class CraftingCritical extends AbstractStatAddEffect
{
	public CraftingCritical(StatSet params)
	{
		super(params, Stat.CRAFTING_CRITICAL);
	}
}
