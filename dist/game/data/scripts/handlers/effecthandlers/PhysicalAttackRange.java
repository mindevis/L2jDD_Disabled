
package handlers.effecthandlers;

import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.stats.Stat;

/**
 * @author Sdw
 */
public class PhysicalAttackRange extends AbstractStatEffect
{
	public PhysicalAttackRange(StatSet params)
	{
		super(params, Stat.PHYSICAL_ATTACK_RANGE);
	}
}
