
package handlers.playeractions;

import org.l2jdd.gameserver.handler.IPlayerActionHandler;
import org.l2jdd.gameserver.model.ActionDataHolder;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.network.SystemMessageId;

/**
 * Servitor stop action player action handler.
 * @author Nik
 */
public class ServitorStop implements IPlayerActionHandler
{
	@Override
	public void useAction(PlayerInstance player, ActionDataHolder data, boolean ctrlPressed, boolean shiftPressed)
	{
		if (!player.hasServitors())
		{
			player.sendPacket(SystemMessageId.YOU_DO_NOT_HAVE_A_SERVITOR);
			return;
		}
		
		player.getServitors().values().forEach(s ->
		{
			if (s.isBetrayed())
			{
				player.sendPacket(SystemMessageId.YOUR_PET_SERVITOR_IS_UNRESPONSIVE_AND_WILL_NOT_OBEY_ANY_ORDERS);
				return;
			}
			
			s.cancelAction();
		});
	}
}
