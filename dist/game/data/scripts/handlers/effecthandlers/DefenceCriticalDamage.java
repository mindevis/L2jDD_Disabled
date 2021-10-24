
package handlers.effecthandlers;

import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.stats.Stat;

/**
 * @author Sdw
 */
public class DefenceCriticalDamage extends AbstractStatEffect
{
	public DefenceCriticalDamage(StatSet params)
	{
		super(params, Stat.DEFENCE_CRITICAL_DAMAGE, Stat.DEFENCE_CRITICAL_DAMAGE_ADD);
	}
}
