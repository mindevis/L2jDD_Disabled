
package handlers.effecthandlers;

import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.stats.Stat;

/**
 * @author Sdw
 */
public class PhysicalSkillPower extends AbstractStatEffect
{
	public PhysicalSkillPower(StatSet params)
	{
		super(params, Stat.PHYSICAL_SKILL_POWER, Stat.SKILL_POWER_ADD);
	}
}
