
package handlers.effecthandlers;

import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.stats.Stat;

/**
 * @author Sdw
 */
public class PvpMagicalSkillDamageBonus extends AbstractStatPercentEffect
{
	public PvpMagicalSkillDamageBonus(StatSet params)
	{
		super(params, Stat.PVP_MAGICAL_SKILL_DAMAGE);
	}
}
