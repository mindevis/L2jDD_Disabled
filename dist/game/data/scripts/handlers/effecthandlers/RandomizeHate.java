
package handlers.effecthandlers;

import java.util.ArrayList;
import java.util.List;

import org.l2jdd.commons.util.Rnd;
import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.World;
import org.l2jdd.gameserver.model.actor.Attackable;
import org.l2jdd.gameserver.model.actor.Creature;
import org.l2jdd.gameserver.model.effects.AbstractEffect;
import org.l2jdd.gameserver.model.items.instance.ItemInstance;
import org.l2jdd.gameserver.model.skills.Skill;
import org.l2jdd.gameserver.model.stats.Formulas;

/**
 * Randomize Hate effect implementation.
 */
public class RandomizeHate extends AbstractEffect
{
	private final int _chance;
	
	public RandomizeHate(StatSet params)
	{
		_chance = params.getInt("chance", 100);
	}
	
	@Override
	public boolean calcSuccess(Creature effector, Creature effected, Skill skill)
	{
		return Formulas.calcProbability(_chance, effector, effected, skill);
	}
	
	@Override
	public boolean isInstant()
	{
		return true;
	}
	
	@Override
	public void instant(Creature effector, Creature effected, Skill skill, ItemInstance item)
	{
		if ((effected == effector) || !effected.isAttackable())
		{
			return;
		}
		
		final Attackable effectedMob = (Attackable) effected;
		final List<Creature> targetList = new ArrayList<>();
		World.getInstance().forEachVisibleObject(effected, Creature.class, cha ->
		{
			if ((cha != effectedMob) && (cha != effector))
			{
				// Aggro cannot be transfered to a mob of the same faction.
				if (cha.isAttackable() && ((Attackable) cha).isInMyClan(effectedMob))
				{
					return;
				}
				
				targetList.add(cha);
			}
		});
		// if there is no target, exit function
		if (targetList.isEmpty())
		{
			return;
		}
		
		// Choosing randomly a new target
		final Creature target = targetList.get(Rnd.get(targetList.size()));
		final int hate = effectedMob.getHating(effector);
		effectedMob.stopHating(effector);
		effectedMob.addDamageHate(target, 0, hate);
	}
}