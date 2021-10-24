
package handlers.effecthandlers;

import java.util.EnumMap;
import java.util.Map;
import java.util.Map.Entry;

import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.actor.Creature;
import org.l2jdd.gameserver.model.effects.AbstractEffect;
import org.l2jdd.gameserver.model.items.instance.ItemInstance;
import org.l2jdd.gameserver.model.skills.Skill;
import org.l2jdd.gameserver.model.stats.TraitType;

/**
 * Attack Trait effect implementation.
 * @author NosBit
 */
public class AttackTrait extends AbstractEffect
{
	private final Map<TraitType, Float> _attackTraits = new EnumMap<>(TraitType.class);
	
	public AttackTrait(StatSet params)
	{
		if (params.isEmpty())
		{
			LOGGER.warning(getClass().getSimpleName() + ": this effect must have parameters!");
			return;
		}
		
		for (Entry<String, Object> param : params.getSet().entrySet())
		{
			_attackTraits.put(TraitType.valueOf(param.getKey()), Float.parseFloat((String) param.getValue()) / 100);
		}
	}
	
	@Override
	public void onStart(Creature effector, Creature effected, Skill skill, ItemInstance item)
	{
		for (Entry<TraitType, Float> trait : _attackTraits.entrySet())
		{
			effected.getStat().mergeAttackTrait(trait.getKey(), trait.getValue().floatValue());
		}
	}
	
	@Override
	public void onExit(Creature effector, Creature effected, Skill skill)
	{
		for (Entry<TraitType, Float> trait : _attackTraits.entrySet())
		{
			effected.getStat().removeAttackTrait(trait.getKey(), trait.getValue().floatValue());
		}
	}
}
