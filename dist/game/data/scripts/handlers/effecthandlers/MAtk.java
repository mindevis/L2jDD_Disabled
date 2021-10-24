
package handlers.effecthandlers;

import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.stats.Stat;

/**
 * @author Sdw
 */
public class MAtk extends AbstractStatEffect
{
	public MAtk(StatSet params)
	{
		super(params, Stat.MAGIC_ATTACK);
	}
}
