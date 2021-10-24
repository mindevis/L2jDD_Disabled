
package handlers.effecthandlers;

import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.stats.Stat;

/**
 * @author Sdw
 */
public class PvpPhysicalSkillDamageBonus extends AbstractStatPercentEffect
{
	public PvpPhysicalSkillDamageBonus(StatSet params)
	{
		super(params, Stat.PVP_PHYSICAL_SKILL_DAMAGE);
	}
}
