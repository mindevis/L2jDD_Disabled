
package handlers.effecthandlers;

import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.actor.Creature;
import org.l2jdd.gameserver.model.effects.AbstractEffect;
import org.l2jdd.gameserver.model.items.instance.ItemInstance;
import org.l2jdd.gameserver.model.skills.Skill;

/**
 * Unsummon my servitors effect implementation.
 * @author Nik
 */
public class UnsummonServitors extends AbstractEffect
{
	public UnsummonServitors(StatSet params)
	{
	}
	
	@Override
	public boolean isInstant()
	{
		return true;
	}
	
	@Override
	public void instant(Creature effector, Creature effected, Skill skill, ItemInstance item)
	{
		if (effector.hasServitors())
		{
			effector.getServitors().values().forEach(servitor ->
			{
				servitor.abortAttack();
				servitor.abortCast();
				servitor.stopAllEffects();
				
				servitor.unSummon(effector.getActingPlayer());
			});
		}
	}
}
