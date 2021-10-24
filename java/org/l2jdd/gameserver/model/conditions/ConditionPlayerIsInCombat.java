
package org.l2jdd.gameserver.model.conditions;

import org.l2jdd.gameserver.model.actor.Creature;
import org.l2jdd.gameserver.model.items.Item;
import org.l2jdd.gameserver.model.skills.Skill;
import org.l2jdd.gameserver.taskmanager.AttackStanceTaskManager;

/**
 * @author Sdw
 */
public class ConditionPlayerIsInCombat extends Condition
{
	private final boolean _value;
	
	public ConditionPlayerIsInCombat(boolean value)
	{
		_value = value;
	}
	
	@Override
	public boolean testImpl(Creature effector, Creature effected, Skill skill, Item item)
	{
		final boolean isInCombat = !AttackStanceTaskManager.getInstance().hasAttackStanceTask(effector);
		return _value == isInCombat;
	}
}
