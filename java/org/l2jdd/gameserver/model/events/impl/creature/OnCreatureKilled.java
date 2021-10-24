
package org.l2jdd.gameserver.model.events.impl.creature;

import org.l2jdd.gameserver.model.actor.Creature;
import org.l2jdd.gameserver.model.events.EventType;
import org.l2jdd.gameserver.model.events.impl.IBaseEvent;

/**
 * An instantly executed event when Creature kills Creature.
 * @author UnAfraid
 */
public class OnCreatureKilled implements IBaseEvent
{
	private final Creature _attacker;
	private final Creature _target;
	
	public OnCreatureKilled(Creature attacker, Creature target)
	{
		_attacker = attacker;
		_target = target;
	}
	
	public Creature getAttacker()
	{
		return _attacker;
	}
	
	public Creature getTarget()
	{
		return _target;
	}
	
	@Override
	public EventType getType()
	{
		return EventType.ON_CREATURE_KILLED;
	}
}