
package handlers.effecthandlers;

import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.stats.Stat;

/**
 * @author Sdw
 */
public class CpRegen extends AbstractStatEffect
{
	public CpRegen(StatSet params)
	{
		super(params, Stat.REGENERATE_CP_RATE);
	}
}
