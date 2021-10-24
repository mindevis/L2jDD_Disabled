
package handlers.effecthandlers;

import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.stats.Stat;

/**
 * @author Sdw
 */
public class PhysicalAttackSpeed extends AbstractStatEffect
{
	public PhysicalAttackSpeed(StatSet params)
	{
		super(params, Stat.PHYSICAL_ATTACK_SPEED);
	}
}
