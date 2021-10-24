
package handlers.effecthandlers;

import org.l2jdd.commons.util.Rnd;
import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.actor.Creature;
import org.l2jdd.gameserver.model.effects.AbstractEffect;
import org.l2jdd.gameserver.model.items.instance.ItemInstance;
import org.l2jdd.gameserver.model.skills.Skill;
import org.l2jdd.gameserver.model.stats.Formulas;

/**
 * Skill Turning effect implementation.
 */
public class SkillTurningOverTime extends AbstractEffect
{
	private final int _chance;
	private final boolean _staticChance;
	
	public SkillTurningOverTime(StatSet params)
	{
		_chance = params.getInt("chance", 100);
		_staticChance = params.getBoolean("staticChance", false);
		setTicks(params.getInt("ticks"));
	}
	
	@Override
	public boolean onActionTime(Creature effector, Creature effected, Skill skill, ItemInstance item)
	{
		if ((effected == null) || (effected == effector) || effected.isRaid())
		{
			return false;
		}
		
		final boolean skillSuccess = _staticChance ? Formulas.calcProbability(_chance, effector, effected, skill) : (Rnd.get(100) < _chance);
		if (skillSuccess)
		{
			effected.breakCast();
		}
		
		return super.onActionTime(effector, effected, skill, item);
	}
}