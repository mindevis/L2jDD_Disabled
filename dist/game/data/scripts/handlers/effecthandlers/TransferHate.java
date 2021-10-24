
package handlers.effecthandlers;

import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.World;
import org.l2jdd.gameserver.model.actor.Attackable;
import org.l2jdd.gameserver.model.actor.Creature;
import org.l2jdd.gameserver.model.effects.AbstractEffect;
import org.l2jdd.gameserver.model.items.instance.ItemInstance;
import org.l2jdd.gameserver.model.skills.Skill;
import org.l2jdd.gameserver.model.stats.Formulas;
import org.l2jdd.gameserver.util.Util;

/**
 * Transfer Hate effect implementation.
 * @author Adry_85
 */
public class TransferHate extends AbstractEffect
{
	private final int _chance;
	
	public TransferHate(StatSet params)
	{
		_chance = params.getInt("chance", 100);
	}
	
	@Override
	public boolean calcSuccess(Creature effector, Creature effected, Skill skill)
	{
		return Formulas.calcProbability(_chance, effector, effected, skill);
	}
	
	@Override
	public boolean canStart(Creature effector, Creature effected, Skill skill)
	{
		return Util.checkIfInRange(skill.getEffectRange(), effector, effected, true);
	}
	
	@Override
	public boolean isInstant()
	{
		return true;
	}
	
	@Override
	public void instant(Creature effector, Creature effected, Skill skill, ItemInstance item)
	{
		World.getInstance().forEachVisibleObjectInRange(effector, Attackable.class, skill.getAffectRange(), hater ->
		{
			if (hater.isDead())
			{
				return;
			}
			final int hate = hater.getHating(effector);
			if (hate <= 0)
			{
				return;
			}
			
			hater.reduceHate(effector, -hate);
			hater.addDamageHate(effected, 0, hate);
		});
	}
}
