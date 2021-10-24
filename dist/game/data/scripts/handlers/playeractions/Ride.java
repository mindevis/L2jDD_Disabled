
package handlers.playeractions;

import org.l2jdd.gameserver.handler.IPlayerActionHandler;
import org.l2jdd.gameserver.model.ActionDataHolder;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;

/**
 * Mount/Dismount player action handler.
 * @author Nik
 */
public class Ride implements IPlayerActionHandler
{
	@Override
	public void useAction(PlayerInstance player, ActionDataHolder data, boolean ctrlPressed, boolean shiftPressed)
	{
		player.mountPlayer(player.getPet());
	}
}
