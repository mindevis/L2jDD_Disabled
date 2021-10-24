
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
 * Defence Trait effect implementation.
 * @author NosBit
 */
public class DefenceTrait extends AbstractEffect
{
	private final Map<TraitType, Float> _defenceTraits = new EnumMap<>(TraitType.class);
	
	public DefenceTrait(StatSet params)
	{
		if (params.isEmpty())
		{
			LOGGER.warning(getClass().getSimpleName() + ": must have parameters.");
			return;
		}
		
		for (Entry<String, Object> param : params.getSet().entrySet())
		{
			_defenceTraits.put(TraitType.valueOf(param.getKey()), Float.parseFloat((String) param.getValue()) / 100);
		}
	}
	
	@Override
	public void onStart(Creature effector, Creature effected, Skill skill, ItemInstance item)
	{
		for (Entry<TraitType, Float> trait : _defenceTraits.entrySet())
		{
			if (trait.getValue().floatValue() < 1.0f)
			{
				effected.getStat().mergeDefenceTrait(trait.getKey(), trait.getValue().floatValue());
			}
			else
			{
				effected.getStat().mergeInvulnerableTrait(trait.getKey());
			}
		}
	}
	
	@Override
	public void onExit(Creature effector, Creature effected, Skill skill)
	{
		for (Entry<TraitType, Float> trait : _defenceTraits.entrySet())
		{
			if (trait.getValue().floatValue() < 1.0f)
			{
				effected.getStat().removeDefenceTrait(trait.getKey(), trait.getValue().floatValue());
			}
			else
			{
				effected.getStat().removeInvulnerableTrait(trait.getKey());
			}
		}
	}
}
