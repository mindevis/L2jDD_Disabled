
package handlers.effecthandlers;

import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.stats.Stat;

/**
 * @author Mobius
 */
public class PveRaidMagicalSkillDamageBonus extends AbstractStatPercentEffect
{
	public PveRaidMagicalSkillDamageBonus(StatSet params)
	{
		super(params, Stat.PVE_RAID_MAGICAL_SKILL_DAMAGE);
	}
}
