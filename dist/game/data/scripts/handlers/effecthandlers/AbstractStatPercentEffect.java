
package handlers.effecthandlers;

import org.l2jdd.gameserver.enums.StatModifierType;
import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.actor.Creature;
import org.l2jdd.gameserver.model.effects.AbstractEffect;
import org.l2jdd.gameserver.model.skills.Skill;
import org.l2jdd.gameserver.model.stats.Stat;

/**
 * @author Mobius
 */
public class AbstractStatPercentEffect extends AbstractEffect
{
	private final Stat _stat;
	protected final double _amount;
	
	public AbstractStatPercentEffect(StatSet params, Stat stat)
	{
		_stat = stat;
		_amount = params.getDouble("amount", 1);
		if (params.getEnum("mode", StatModifierType.class, StatModifierType.PER) != StatModifierType.PER)
		{
			LOGGER.warning(getClass().getSimpleName() + " can only use PER mode.");
		}
	}
	
	@Override
	public void pump(Creature effected, Skill skill)
	{
		effected.getStat().mergeMul(_stat, (_amount / 100) + 1);
	}
}
