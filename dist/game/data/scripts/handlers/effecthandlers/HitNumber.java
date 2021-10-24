
package handlers.effecthandlers;

import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.stats.Stat;

/**
 * @author Sdw
 */
public class HitNumber extends AbstractStatEffect
{
	public HitNumber(StatSet params)
	{
		super(params, Stat.ATTACK_COUNT_MAX);
	}
}
