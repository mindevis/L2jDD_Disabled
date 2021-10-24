
package handlers.effecthandlers;

import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.actor.Creature;
import org.l2jdd.gameserver.model.actor.Playable;
import org.l2jdd.gameserver.model.effects.AbstractEffect;
import org.l2jdd.gameserver.model.items.instance.ItemInstance;
import org.l2jdd.gameserver.model.skills.Skill;

/**
 * Target Me effect implementation.
 * @author -Nemesiss-
 */
public class TargetMe extends AbstractEffect
{
	public TargetMe(StatSet params)
	{
	}
	
	@Override
	public void onExit(Creature effector, Creature effected, Skill skill)
	{
		if (effected.isPlayable())
		{
			((Playable) effected).setLockedTarget(null);
		}
	}
	
	@Override
	public void onStart(Creature effector, Creature effected, Skill skill, ItemInstance item)
	{
		if (effected.isPlayable())
		{
			if (effected.getTarget() != effector)
			{
				effected.setTarget(effector);
			}
			
			((Playable) effected).setLockedTarget(effector);
		}
	}
}
