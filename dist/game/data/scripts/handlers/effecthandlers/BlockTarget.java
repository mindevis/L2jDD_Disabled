
package handlers.effecthandlers;

import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.World;
import org.l2jdd.gameserver.model.actor.Creature;
import org.l2jdd.gameserver.model.effects.AbstractEffect;
import org.l2jdd.gameserver.model.items.instance.ItemInstance;
import org.l2jdd.gameserver.model.skills.Skill;

/**
 * @author UnAfraid
 */
public class BlockTarget extends AbstractEffect
{
	public BlockTarget(StatSet params)
	{
	}
	
	@Override
	public void onStart(Creature effector, Creature effected, Skill skill, ItemInstance item)
	{
		effected.setTargetable(false);
		World.getInstance().forEachVisibleObject(effected, Creature.class, target ->
		{
			if ((target.getTarget() == effected))
			{
				target.setTarget(null);
			}
		});
	}
	
	@Override
	public void onExit(Creature effector, Creature effected, Skill skill)
	{
		effected.setTargetable(true);
	}
}
