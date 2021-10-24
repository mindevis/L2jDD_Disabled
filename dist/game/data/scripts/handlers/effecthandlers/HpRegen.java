
package handlers.effecthandlers;

import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.stats.Stat;

/**
 * @author Sdw
 */
public class HpRegen extends AbstractStatEffect
{
	public HpRegen(StatSet params)
	{
		super(params, Stat.REGENERATE_HP_RATE);
	}
}
