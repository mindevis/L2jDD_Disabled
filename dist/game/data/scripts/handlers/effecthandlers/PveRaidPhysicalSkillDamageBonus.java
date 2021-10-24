
package handlers.effecthandlers;

import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.stats.Stat;

/**
 * @author Mobius
 */
public class PveRaidPhysicalSkillDamageBonus extends AbstractStatPercentEffect
{
	public PveRaidPhysicalSkillDamageBonus(StatSet params)
	{
		super(params, Stat.PVE_RAID_PHYSICAL_SKILL_DAMAGE);
	}
}
