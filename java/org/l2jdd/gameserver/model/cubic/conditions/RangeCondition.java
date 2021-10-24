
package org.l2jdd.gameserver.model.cubic.conditions;

import org.l2jdd.gameserver.model.WorldObject;
import org.l2jdd.gameserver.model.actor.Creature;
import org.l2jdd.gameserver.model.cubic.CubicInstance;

/**
 * @author Sdw
 */
public class RangeCondition implements ICubicCondition
{
	private final int _range;
	
	public RangeCondition(int range)
	{
		_range = range;
	}
	
	@Override
	public boolean test(CubicInstance cubic, Creature owner, WorldObject target)
	{
		return owner.calculateDistance2D(target) <= _range;
	}
}
