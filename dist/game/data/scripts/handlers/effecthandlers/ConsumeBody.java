
package handlers.effecthandlers;

import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.actor.Creature;
import org.l2jdd.gameserver.model.actor.Npc;
import org.l2jdd.gameserver.model.effects.AbstractEffect;
import org.l2jdd.gameserver.model.items.instance.ItemInstance;
import org.l2jdd.gameserver.model.skills.Skill;

/**
 * Consume Body effect implementation.
 * @author Mobius
 */
public class ConsumeBody extends AbstractEffect
{
	public ConsumeBody(StatSet params)
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
		if (!effected.isDead() //
			|| (effector.getTarget() != effected) //
			|| (!effected.isNpc() && !effected.isSummon()) //
			|| (effected.isSummon() && (effector != effected.getActingPlayer())))
		{
			return;
		}
		
		if (effected.isNpc())
		{
			((Npc) effected).endDecayTask();
		}
	}
}
