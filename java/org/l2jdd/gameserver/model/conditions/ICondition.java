
package org.l2jdd.gameserver.model.conditions;

import org.l2jdd.gameserver.model.WorldObject;
import org.l2jdd.gameserver.model.actor.Creature;

/**
 * @author Sdw
 */
public interface ICondition
{
	boolean test(Creature creature, WorldObject object);
}
