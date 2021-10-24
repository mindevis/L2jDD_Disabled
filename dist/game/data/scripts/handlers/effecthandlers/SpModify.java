
package handlers.effecthandlers;

import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.stats.Stat;

/**
 * @author Sdw
 */
public class SpModify extends AbstractStatAddEffect
{
	public SpModify(StatSet params)
	{
		super(params, Stat.BONUS_SP);
	}
}
