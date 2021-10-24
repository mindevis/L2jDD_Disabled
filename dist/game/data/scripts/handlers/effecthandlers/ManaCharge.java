
package handlers.effecthandlers;

import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.stats.Stat;

/**
 * @author Sdw
 */
public class ManaCharge extends AbstractStatAddEffect
{
	public ManaCharge(StatSet params)
	{
		super(params, Stat.MANA_CHARGE);
	}
}
