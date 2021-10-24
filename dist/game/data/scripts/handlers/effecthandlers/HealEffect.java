
package handlers.effecthandlers;

import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.stats.Stat;

/**
 * @author Sdw, Mobius
 */
public class HealEffect extends AbstractStatEffect
{
	public HealEffect(StatSet params)
	{
		super(params, Stat.HEAL_EFFECT, Stat.HEAL_EFFECT_ADD);
	}
}
