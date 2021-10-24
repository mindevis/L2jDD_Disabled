
package handlers.effecthandlers;

import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.actor.Creature;
import org.l2jdd.gameserver.model.actor.Summon;
import org.l2jdd.gameserver.model.effects.AbstractEffect;
import org.l2jdd.gameserver.model.items.instance.ItemInstance;
import org.l2jdd.gameserver.model.skills.Skill;

/**
 * Immobile Pet Buff effect implementation.
 * @author demonia
 */
public class ImmobilePetBuff extends AbstractEffect
{
	public ImmobilePetBuff(StatSet params)
	{
	}
	
	@Override
	public void onExit(Creature effector, Creature effected, Skill skill)
	{
		effected.setImmobilized(false);
	}
	
	@Override
	public void onStart(Creature effector, Creature effected, Skill skill, ItemInstance item)
	{
		if (effected.isSummon() && effector.isPlayer() && (((Summon) effected).getOwner() == effector))
		{
			effected.setImmobilized(true);
		}
	}
}