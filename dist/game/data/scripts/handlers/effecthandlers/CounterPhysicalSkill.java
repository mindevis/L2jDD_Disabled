
package handlers.effecthandlers;

import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.stats.Stat;

/**
 * @author Sdw
 */
public class CounterPhysicalSkill extends AbstractStatAddEffect
{
	public CounterPhysicalSkill(StatSet params)
	{
		super(params, Stat.VENGEANCE_SKILL_PHYSICAL_DAMAGE);
	}
}
