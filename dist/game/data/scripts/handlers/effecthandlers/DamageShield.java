
package handlers.effecthandlers;

import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.stats.Stat;

/**
 * @author Sdw
 */
public class DamageShield extends AbstractStatAddEffect
{
	public DamageShield(StatSet params)
	{
		super(params, Stat.REFLECT_DAMAGE_PERCENT);
	}
}
