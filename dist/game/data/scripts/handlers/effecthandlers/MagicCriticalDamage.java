
package handlers.effecthandlers;

import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.stats.Stat;

/**
 * @author Sdw
 */
public class MagicCriticalDamage extends AbstractStatEffect
{
	public MagicCriticalDamage(StatSet params)
	{
		super(params, Stat.MAGIC_CRITICAL_DAMAGE, Stat.MAGIC_CRITICAL_DAMAGE_ADD);
	}
}
