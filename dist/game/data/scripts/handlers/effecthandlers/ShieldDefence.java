
package handlers.effecthandlers;

import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.stats.Stat;

/**
 * @author Sdw
 */
public class ShieldDefence extends AbstractStatEffect
{
	public ShieldDefence(StatSet params)
	{
		super(params, Stat.SHIELD_DEFENCE);
	}
}
