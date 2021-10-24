
package handlers.effecthandlers;

import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.stats.Stat;

/**
 * @author Sdw
 */
public class MagicalDefence extends AbstractStatEffect
{
	public MagicalDefence(StatSet params)
	{
		super(params, Stat.MAGICAL_DEFENCE);
	}
}
