
package org.l2jdd.gameserver.model.eventengine;

/**
 * @author UnAfraid
 */
public interface IConditionalEventScheduler
{
	boolean test();
	
	void run();
}
