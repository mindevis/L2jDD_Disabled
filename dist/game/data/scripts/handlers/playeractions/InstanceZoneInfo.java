
package handlers.playeractions;

import org.l2jdd.gameserver.handler.IPlayerActionHandler;
import org.l2jdd.gameserver.model.ActionDataHolder;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.network.serverpackets.ExInzoneWaiting;

/**
 * Instance Zone Info player action handler.
 * @author St3eT
 */
public class InstanceZoneInfo implements IPlayerActionHandler
{
	@Override
	public void useAction(PlayerInstance player, ActionDataHolder data, boolean ctrlPressed, boolean shiftPressed)
	{
		player.sendPacket(new ExInzoneWaiting(player, false));
	}
}
