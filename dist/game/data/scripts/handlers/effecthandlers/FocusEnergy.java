
package handlers.effecthandlers;

import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.stats.Stat;

/**
 * @author Sdw
 */
public class FocusEnergy extends AbstractStatAddEffect
{
	public FocusEnergy(StatSet params)
	{
		super(params, Stat.MAX_MOMENTUM);
	}
}
