
package handlers.effecthandlers;

import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.stats.Stat;

/**
 * @author Sdw
 */
public class MagicalEvasion extends AbstractStatEffect
{
	public MagicalEvasion(StatSet params)
	{
		super(params, Stat.MAGIC_EVASION_RATE);
	}
}
