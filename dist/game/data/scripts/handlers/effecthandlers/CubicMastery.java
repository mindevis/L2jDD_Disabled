
package handlers.effecthandlers;

import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.stats.Stat;

/**
 * @author Sdw
 */
public class CubicMastery extends AbstractStatAddEffect
{
	public CubicMastery(StatSet params)
	{
		super(params, Stat.MAX_CUBIC);
	}
}
