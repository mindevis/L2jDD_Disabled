
package handlers.effecthandlers;

import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.stats.Stat;

/**
 * @author Sdw
 */
public class DamageShieldResist extends AbstractStatAddEffect
{
	public DamageShieldResist(StatSet params)
	{
		super(params, Stat.REFLECT_DAMAGE_PERCENT_DEFENSE);
	}
}
