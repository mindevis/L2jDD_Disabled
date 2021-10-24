
package handlers.effecthandlers;

import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.stats.Stat;

/**
 * @author Sdw
 */
public class PhysicalDefence extends AbstractConditionalHpEffect
{
	public PhysicalDefence(StatSet params)
	{
		super(params, Stat.PHYSICAL_DEFENCE);
	}
}
