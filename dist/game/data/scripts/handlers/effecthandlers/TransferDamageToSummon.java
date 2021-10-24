
package handlers.effecthandlers;

import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.stats.Stat;

/**
 * @author Sdw
 */
public class TransferDamageToSummon extends AbstractStatAddEffect
{
	public TransferDamageToSummon(StatSet params)
	{
		super(params, Stat.TRANSFER_DAMAGE_SUMMON_PERCENT);
	}
}
