
package handlers.effecthandlers;

import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.stats.Stat;

/**
 * @author Sdw
 */
public class AreaDamage extends AbstractStatAddEffect
{
	public AreaDamage(StatSet params)
	{
		super(params, Stat.DAMAGE_ZONE_VULN);
	}
}
