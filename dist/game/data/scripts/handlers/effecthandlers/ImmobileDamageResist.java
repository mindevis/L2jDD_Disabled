
package handlers.effecthandlers;

import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.stats.Stat;

/**
 * Resist damage while immobile.
 * @author Mobius
 */
public class ImmobileDamageResist extends AbstractStatPercentEffect
{
	public ImmobileDamageResist(StatSet params)
	{
		super(params, Stat.IMMOBILE_DAMAGE_RESIST);
	}
}
