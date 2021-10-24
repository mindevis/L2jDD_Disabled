
package handlers.effecthandlers;

import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.stats.Stat;

/**
 * @author Sdw
 */
public class ShieldDefenceRate extends AbstractStatEffect
{
	public ShieldDefenceRate(StatSet params)
	{
		super(params, Stat.SHIELD_DEFENCE_RATE);
	}
}
