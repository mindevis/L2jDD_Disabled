
package org.l2jdd.gameserver.script;

/**
 * @author Luis Arias
 */
public interface EngineInterface
{
	void addEventDrop(int[] items, int[] count, double chance, DateRange range);
	
	void onPlayerLogin(String message, DateRange range);
}
