
package org.l2jdd.gameserver.model.conditions;

import org.l2jdd.gameserver.enums.CastleSide;
import org.l2jdd.gameserver.model.actor.Creature;
import org.l2jdd.gameserver.model.items.Item;
import org.l2jdd.gameserver.model.skills.Skill;

/**
 * The Class ConditionPlayerIsOnSide.
 * @author St3eT
 */
public class ConditionPlayerIsOnSide extends Condition
{
	private final CastleSide _side;
	
	/**
	 * Instantiates a new condition player race.
	 * @param side the allowed Castle side.
	 */
	public ConditionPlayerIsOnSide(CastleSide side)
	{
		_side = side;
	}
	
	@Override
	public boolean testImpl(Creature effector, Creature effected, Skill skill, Item item)
	{
		if ((effector == null) || !effector.isPlayer())
		{
			return false;
		}
		return effector.getActingPlayer().getPlayerSide() == _side;
	}
}
