
package org.l2jdd.gameserver.model.conditions;

import org.l2jdd.gameserver.model.actor.Creature;
import org.l2jdd.gameserver.model.items.Item;
import org.l2jdd.gameserver.model.skills.Skill;

/**
 * The Class ConditionPlayerAgathionId.
 */
public class ConditionPlayerAgathionId extends Condition
{
	private final int _agathionId;
	
	/**
	 * Instantiates a new condition player agathion id.
	 * @param agathionId the agathion id
	 */
	public ConditionPlayerAgathionId(int agathionId)
	{
		_agathionId = agathionId;
	}
	
	@Override
	public boolean testImpl(Creature effector, Creature effected, Skill skill, Item item)
	{
		return (effector.getActingPlayer() != null) && (effector.getActingPlayer().getAgathionId() == _agathionId);
	}
}
