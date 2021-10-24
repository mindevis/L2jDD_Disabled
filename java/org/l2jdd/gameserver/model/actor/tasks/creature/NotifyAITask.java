
package org.l2jdd.gameserver.model.actor.tasks.creature;

import org.l2jdd.gameserver.ai.CtrlEvent;
import org.l2jdd.gameserver.model.actor.Creature;

/**
 * Task dedicated to notify character's AI
 * @author xban1x
 */
public class NotifyAITask implements Runnable
{
	private final Creature _creature;
	private final CtrlEvent _event;
	
	public NotifyAITask(Creature creature, CtrlEvent event)
	{
		_creature = creature;
		_event = event;
	}
	
	@Override
	public void run()
	{
		if (_creature != null)
		{
			_creature.getAI().notifyEvent(_event, null);
		}
	}
}
