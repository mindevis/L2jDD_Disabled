
package org.l2jdd.gameserver.model.conditions;

import org.l2jdd.gameserver.model.actor.Creature;
import org.l2jdd.gameserver.model.items.Item;
import org.l2jdd.gameserver.model.skills.Skill;

/**
 * @author NosBit
 */
public class ConditionUsingSlotType extends Condition
{
	private final int _mask;
	
	public ConditionUsingSlotType(int mask)
	{
		_mask = mask;
	}
	
	@Override
	public boolean testImpl(Creature effector, Creature effected, Skill skill, Item item)
	{
		if ((effector == null) || !effector.isPlayer())
		{
			return false;
		}
		return (effector.getActiveWeaponItem().getBodyPart() & _mask) != 0;
	}
}
