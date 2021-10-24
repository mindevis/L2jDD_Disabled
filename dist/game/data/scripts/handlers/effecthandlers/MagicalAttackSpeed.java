
package handlers.effecthandlers;

import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.stats.Stat;

/**
 * @author Sdw
 */
public class MagicalAttackSpeed extends AbstractStatEffect
{
	public MagicalAttackSpeed(StatSet params)
	{
		super(params, Stat.MAGIC_ATTACK_SPEED);
	}
}
