
package handlers.effecthandlers;

import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.stats.Stat;

/**
 * Bonus damage to immobile targets.
 * @author Mobius
 */
public class ImmobileDamageBonus extends AbstractStatPercentEffect
{
	public ImmobileDamageBonus(StatSet params)
	{
		super(params, Stat.IMMOBILE_DAMAGE_BONUS);
	}
}
