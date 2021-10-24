
package handlers.effecthandlers;

import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.stats.Stat;

/**
 * @author Sdw
 */
public class DefenceMagicCriticalDamage extends AbstractStatEffect
{
	public DefenceMagicCriticalDamage(StatSet params)
	{
		super(params, Stat.DEFENCE_MAGIC_CRITICAL_DAMAGE, Stat.DEFENCE_MAGIC_CRITICAL_DAMAGE_ADD);
	}
}
