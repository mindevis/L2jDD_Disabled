
package handlers.effecthandlers;

import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.stats.Stat;

/**
 * @author Sdw
 */
public class MpShield extends AbstractStatAddEffect
{
	public MpShield(StatSet params)
	{
		super(params, Stat.MANA_SHIELD_PERCENT);
	}
}
