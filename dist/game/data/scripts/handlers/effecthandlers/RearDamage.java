
package handlers.effecthandlers;

import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.stats.Stat;

/**
 * @author Mobius
 */
public class RearDamage extends AbstractStatPercentEffect
{
	public RearDamage(StatSet params)
	{
		super(params, Stat.REAR_DAMAGE_RATE);
	}
}
