
package org.l2jdd.gameserver.model.conditions;

import org.l2jdd.gameserver.model.actor.Creature;
import org.l2jdd.gameserver.model.items.Item;
import org.l2jdd.gameserver.model.skills.Skill;

/**
 * @author DS
 */
public class ConditionTargetPlayable extends Condition
{
	@Override
	public boolean testImpl(Creature effector, Creature effected, Skill skill, Item item)
	{
		return (effected != null) && effected.isPlayable();
	}
}
