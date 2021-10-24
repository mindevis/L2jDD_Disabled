
package org.l2jdd.gameserver.model.conditions;

import org.l2jdd.gameserver.model.actor.Creature;
import org.l2jdd.gameserver.model.items.Item;
import org.l2jdd.gameserver.model.skills.Skill;

/**
 * The Class ConditionInventory.
 * @author mkizub
 */
public abstract class ConditionInventory extends Condition
{
	protected final int _slot;
	
	/**
	 * Instantiates a new condition inventory.
	 * @param slot the slot
	 */
	public ConditionInventory(int slot)
	{
		_slot = slot;
	}
	
	/**
	 * Test impl.
	 * @return true, if successful
	 */
	@Override
	public abstract boolean testImpl(Creature effector, Creature effected, Skill skill, Item item);
}
