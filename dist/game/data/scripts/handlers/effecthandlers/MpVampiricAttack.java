
package handlers.effecthandlers;

import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.stats.Stat;

/**
 * @author Sdw
 */
public class MpVampiricAttack extends AbstractStatAddEffect
{
	public MpVampiricAttack(StatSet params)
	{
		super(params, Stat.ABSORB_MANA_DAMAGE_PERCENT);
	}
}
