
package handlers.effecthandlers;

import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.stats.Stat;

/**
 * @author Sdw
 */
public class PAtk extends AbstractConditionalHpEffect
{
	public PAtk(StatSet params)
	{
		super(params, Stat.PHYSICAL_ATTACK);
	}
}
