
package handlers.effecthandlers;

import java.util.ArrayList;
import java.util.List;

import org.l2jdd.commons.util.Rnd;
import org.l2jdd.gameserver.ai.CtrlEvent;
import org.l2jdd.gameserver.ai.CtrlIntention;
import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.World;
import org.l2jdd.gameserver.model.actor.Creature;
import org.l2jdd.gameserver.model.effects.AbstractEffect;
import org.l2jdd.gameserver.model.effects.EffectFlag;
import org.l2jdd.gameserver.model.items.instance.ItemInstance;
import org.l2jdd.gameserver.model.skills.Skill;
import org.l2jdd.gameserver.model.stats.Formulas;

/**
 * Confuse effect implementation.
 * @author littlecrow
 */
public class Confuse extends AbstractEffect
{
	private final int _chance;
	
	public Confuse(StatSet params)
	{
		_chance = params.getInt("chance", 100);
	}
	
	@Override
	public boolean calcSuccess(Creature effector, Creature effected, Skill skill)
	{
		return Formulas.calcProbability(_chance, effector, effected, skill);
	}
	
	@Override
	public long getEffectFlags()
	{
		return EffectFlag.CONFUSED.getMask();
	}
	
	@Override
	public boolean isInstant()
	{
		return true;
	}
	
	@Override
	public void instant(Creature effector, Creature effected, Skill skill, ItemInstance item)
	{
		effected.getAI().notifyEvent(CtrlEvent.EVT_CONFUSED);
		
		final List<Creature> targetList = new ArrayList<>();
		// Getting the possible targets
		
		World.getInstance().forEachVisibleObject(effected, Creature.class, targetList::add);
		
		// if there is no target, exit function
		if (!targetList.isEmpty())
		{
			// Choosing randomly a new target
			final Creature target = targetList.get(Rnd.get(targetList.size()));
			// Attacking the target
			effected.setTarget(target);
			effected.getAI().setIntention(CtrlIntention.AI_INTENTION_ATTACK, target);
		}
	}
}
