
package handlers.effecthandlers;

import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.stats.Stat;

/**
 * @author Mobius
 */
public class RealDamageResist extends AbstractStatPercentEffect
{
	public RealDamageResist(StatSet params)
	{
		super(params, Stat.REAL_DAMAGE_RESIST);
	}
}
