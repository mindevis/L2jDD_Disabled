
package handlers.effecthandlers;

import org.l2jdd.gameserver.enums.DoorOpenType;
import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.actor.Creature;
import org.l2jdd.gameserver.model.actor.instance.DoorInstance;
import org.l2jdd.gameserver.model.effects.AbstractEffect;
import org.l2jdd.gameserver.model.items.instance.ItemInstance;
import org.l2jdd.gameserver.model.skills.Skill;

/**
 * Detect Hidden Objects effect implementation.
 * @author UnAfraid
 */
public class DetectHiddenObjects extends AbstractEffect
{
	public DetectHiddenObjects(StatSet params)
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
		if (!effected.isDoor())
		{
			return;
		}
		
		final DoorInstance door = (DoorInstance) effected;
		if (door.getTemplate().isStealth())
		{
			door.setMeshIndex(1);
			door.setTargetable(door.getTemplate().getOpenType() != DoorOpenType.NONE);
			door.broadcastStatusUpdate();
		}
	}
}
