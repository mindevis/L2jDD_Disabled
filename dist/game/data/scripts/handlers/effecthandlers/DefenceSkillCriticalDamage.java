
package handlers.effecthandlers;

import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.stats.Stat;

/**
 * Equivalent of DefenceMagicCriticalDamage for physical skills.
 * @author Mobius
 */
public class DefenceSkillCriticalDamage extends AbstractStatEffect
{
	public DefenceSkillCriticalDamage(StatSet params)
	{
		super(params, Stat.DEFENCE_CRITICAL_DAMAGE_SKILL, Stat.DEFENCE_CRITICAL_DAMAGE_SKILL_ADD);
	}
}
