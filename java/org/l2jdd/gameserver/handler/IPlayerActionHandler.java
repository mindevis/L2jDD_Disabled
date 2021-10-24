
package org.l2jdd.gameserver.handler;

import org.l2jdd.gameserver.model.ActionDataHolder;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;

/**
 * @author UnAfraid
 */
public interface IPlayerActionHandler
{
	void useAction(PlayerInstance player, ActionDataHolder data, boolean ctrlPressed, boolean shiftPressed);
}