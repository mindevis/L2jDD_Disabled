
package handlers.playeractions;

import org.l2jdd.gameserver.handler.IPlayerActionHandler;
import org.l2jdd.gameserver.instancemanager.AirShipManager;
import org.l2jdd.gameserver.model.ActionDataHolder;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;

/**
 * Airship Action player action handler.
 * @author Nik
 */
public class AirshipAction implements IPlayerActionHandler
{
	@Override
	public void useAction(PlayerInstance player, ActionDataHolder data, boolean ctrlPressed, boolean shiftPressed)
	{
		if (!player.isInAirShip())
		{
			return;
		}
		
		switch (data.getOptionId())
		{
			case 1: // Steer
			{
				if (player.getAirShip().setCaptain(player))
				{
					player.broadcastUserInfo();
				}
				break;
			}
			case 2: // Cancel Control
			{
				if (player.getAirShip().isCaptain(player) && player.getAirShip().setCaptain(null))
				{
					player.broadcastUserInfo();
				}
				break;
			}
			case 3: // Destination Map
			{
				AirShipManager.getInstance().sendAirShipTeleportList(player);
				break;
			}
			case 4: // Exit Airship
			{
				if (player.getAirShip().isCaptain(player))
				{
					if (player.getAirShip().setCaptain(null))
					{
						player.broadcastUserInfo();
					}
				}
				else if (player.getAirShip().isInDock())
				{
					player.getAirShip().oustPlayer(player);
				}
			}
		}
	}
}
