
package org.l2jdd.gameserver.handler;

import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;

public interface IAdminCommandHandler
{
	/**
	 * this is the worker method that is called when someone uses an admin command.
	 * @param player
	 * @param command
	 * @return command success
	 */
	boolean useAdminCommand(String command, PlayerInstance player);
	
	/**
	 * this method is called at initialization to register all the item ids automatically
	 * @return all known itemIds
	 */
	String[] getAdminCommandList();
}
