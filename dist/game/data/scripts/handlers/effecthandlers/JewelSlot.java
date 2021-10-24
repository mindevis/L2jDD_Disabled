
package handlers.effecthandlers;

import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.stats.Stat;

/**
 * @author Sdw
 */
public class JewelSlot extends AbstractStatAddEffect
{
	public JewelSlot(StatSet params)
	{
		super(params, Stat.BROOCH_JEWELS);
	}
}
