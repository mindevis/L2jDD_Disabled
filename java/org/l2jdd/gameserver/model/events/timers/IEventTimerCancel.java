
package org.l2jdd.gameserver.model.events.timers;

/**
 * @author UnAfraid
 * @param <T>
 */
@FunctionalInterface
public interface IEventTimerCancel<T>
{
	/**
	 * Notified upon timer cancellation.
	 * @param holder
	 */
	void onTimerCancel(TimerHolder<T> holder);
}
