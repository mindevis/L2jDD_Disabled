
package handlers.effecthandlers;

import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.stats.Stat;

/**
 * @author Sdw
 */
public class MpRegen extends AbstractStatEffect
{
	public MpRegen(StatSet params)
	{
		super(params, Stat.REGENERATE_MP_RATE);
	}
}
