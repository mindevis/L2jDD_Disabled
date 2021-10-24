
package handlers.effecthandlers;

import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.stats.Stat;

/**
 * @author Sdw
 */
public class PhysicalEvasion extends AbstractConditionalHpEffect
{
	public PhysicalEvasion(StatSet params)
	{
		super(params, Stat.EVASION_RATE);
	}
}
