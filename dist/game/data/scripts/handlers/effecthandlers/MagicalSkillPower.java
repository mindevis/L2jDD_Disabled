
package handlers.effecthandlers;

import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.stats.Stat;

/**
 * @author Mobius
 */
public class MagicalSkillPower extends AbstractStatEffect
{
	public MagicalSkillPower(StatSet params)
	{
		super(params, Stat.MAGICAL_SKILL_POWER, Stat.SKILL_POWER_ADD);
	}
}