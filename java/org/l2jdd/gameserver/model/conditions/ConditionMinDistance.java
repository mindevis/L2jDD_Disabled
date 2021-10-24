
package org.l2jdd.gameserver.model.conditions;

import org.l2jdd.gameserver.geoengine.GeoEngine;
import org.l2jdd.gameserver.model.actor.Creature;
import org.l2jdd.gameserver.model.items.Item;
import org.l2jdd.gameserver.model.skills.Skill;

/**
 * @author Mobius
 */
public class ConditionMinDistance extends Condition
{
	private final int _distance;
	
	public ConditionMinDistance(int distance)
	{
		_distance = distance;
	}
	
	@Override
	public boolean testImpl(Creature effector, Creature effected, Skill skill, Item item)
	{
		return (effected != null) //
			&& (effector.calculateDistance3D(effected) >= _distance) //
			&& GeoEngine.getInstance().canSeeTarget(effector, effected);
	}
}
