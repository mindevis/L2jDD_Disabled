
package org.l2jdd.gameserver.model.events.listeners;

import org.l2jdd.gameserver.model.events.EventType;
import org.l2jdd.gameserver.model.events.ListenersContainer;
import org.l2jdd.gameserver.model.events.impl.IBaseEvent;
import org.l2jdd.gameserver.model.events.returns.AbstractEventReturn;

/**
 * Runnable event listener provides callback operation without any parameters and return object.
 * @author UnAfraid
 */
public class RunnableEventListener extends AbstractEventListener
{
	private final Runnable _callback;
	
	public RunnableEventListener(ListenersContainer container, EventType type, Runnable callback, Object owner)
	{
		super(container, type, owner);
		_callback = callback;
	}
	
	@Override
	public <R extends AbstractEventReturn> R executeEvent(IBaseEvent event, Class<R> returnBackClass)
	{
		_callback.run();
		return null;
	}
}
