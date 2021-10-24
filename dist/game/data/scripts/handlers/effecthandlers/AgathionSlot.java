
package handlers.effecthandlers;

import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.stats.Stat;

/**
 * @author Mobius
 */
public class AgathionSlot extends AbstractStatAddEffect
{
	public AgathionSlot(StatSet params)
	{
		super(params, Stat.AGATHION_SLOTS);
	}
}
