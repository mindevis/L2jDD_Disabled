
package handlers.effecthandlers;

import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.stats.Stat;

/**
 * @author Sdw
 */
public class HateAttack extends AbstractStatPercentEffect
{
	public HateAttack(StatSet params)
	{
		super(params, Stat.HATE_ATTACK);
	}
}
