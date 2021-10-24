
package org.l2jdd.gameserver.model.conditions;

import org.l2jdd.gameserver.model.actor.Creature;
import org.l2jdd.gameserver.model.items.Item;
import org.l2jdd.gameserver.model.skills.Skill;

public class ConditionPlayerDualclass extends Condition
{
	private final boolean _value;
	
	public ConditionPlayerDualclass(boolean value)
	{
		_value = value;
	}
	
	@Override
	public boolean testImpl(Creature effector, Creature effected, Skill skill, Item item)
	{
		if (effector.getActingPlayer() == null)
		{
			return true;
		}
		return effector.getActingPlayer().isDualClassActive() == _value;
	}
}
