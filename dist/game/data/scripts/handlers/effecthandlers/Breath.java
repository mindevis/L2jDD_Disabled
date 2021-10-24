
package handlers.effecthandlers;

import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.stats.Stat;

/**
 * @author Sdw
 */
public class Breath extends AbstractStatEffect
{
	public Breath(StatSet params)
	{
		super(params, Stat.BREATH);
	}
}
