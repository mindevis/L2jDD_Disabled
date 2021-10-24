
package org.l2jdd.gameserver.model.conditions;

import org.l2jdd.gameserver.model.actor.Creature;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.model.items.Item;
import org.l2jdd.gameserver.model.skills.Skill;

/**
 * @author Mobius
 */
public class ConditionMinimumVitalityPoints extends Condition
{
	private final int _count;
	
	public ConditionMinimumVitalityPoints(int count)
	{
		_count = count;
	}
	
	@Override
	public boolean testImpl(Creature effector, Creature effected, Skill skill, Item item)
	{
		final PlayerInstance player = effector.getActingPlayer();
		if (player != null)
		{
			return player.getVitalityPoints() >= _count;
		}
		return false;
	}
}
