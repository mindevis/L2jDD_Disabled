
package handlers.effecthandlers;

import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.stats.Stat;

/**
 * @author quangnguyen
 */
public class CraftRate extends AbstractStatAddEffect
{
	public CraftRate(StatSet params)
	{
		super(params, Stat.CRAFT_RATE);
	}
}
