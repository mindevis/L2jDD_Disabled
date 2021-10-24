
package org.l2jdd.gameserver.model.events.timers;

/**
 * @author UnAfraid
 * @param <T>
 */
@FunctionalInterface
public interface IEventTimerEvent<T>
{
	/**
	 * notified upon timer execution method.
	 * @param holder
	 */
	void onTimerEvent(TimerHolder<T> holder);
}
