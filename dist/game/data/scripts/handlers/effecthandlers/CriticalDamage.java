
package handlers.effecthandlers;

import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.stats.Stat;

/**
 * @author Sdw
 */
public class CriticalDamage extends AbstractStatEffect
{
	public CriticalDamage(StatSet params)
	{
		super(params, Stat.CRITICAL_DAMAGE, Stat.CRITICAL_DAMAGE_ADD);
	}
}
