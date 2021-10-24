
package org.l2jdd.gameserver.model.conditions;

import org.l2jdd.gameserver.model.actor.Creature;
import org.l2jdd.gameserver.model.items.Item;
import org.l2jdd.gameserver.model.skills.Skill;

/**
 * @author Sdw
 */
public class ConditionPlayerInInstance extends Condition
{
	public boolean _inInstance;
	
	public ConditionPlayerInInstance(boolean inInstance)
	{
		_inInstance = inInstance;
	}
	
	@Override
	public boolean testImpl(Creature effector, Creature effected, Skill skill, Item item)
	{
		if (effector.getActingPlayer() == null)
		{
			return false;
		}
		return (effector.getInstanceId() == 0) ? !_inInstance : _inInstance;
	}
}
