
package handlers.playeractions;

import java.util.logging.Logger;

import org.l2jdd.gameserver.enums.PrivateStoreType;
import org.l2jdd.gameserver.handler.IPlayerActionHandler;
import org.l2jdd.gameserver.model.ActionDataHolder;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.model.zone.ZoneId;
import org.l2jdd.gameserver.network.SystemMessageId;
import org.l2jdd.gameserver.network.serverpackets.ActionFailed;
import org.l2jdd.gameserver.network.serverpackets.PrivateStoreManageListBuy;
import org.l2jdd.gameserver.network.serverpackets.PrivateStoreManageListSell;
import org.l2jdd.gameserver.network.serverpackets.RecipeShopManageList;

/**
 * Open/Close private store player action handler.
 * @author Nik
 */
public class PrivateStore implements IPlayerActionHandler
{
	private static final Logger LOGGER = Logger.getLogger(PrivateStore.class.getName());
	
	@Override
	public void useAction(PlayerInstance player, ActionDataHolder data, boolean ctrlPressed, boolean shiftPressed)
	{
		final PrivateStoreType type = PrivateStoreType.findById(data.getOptionId());
		if (type == null)
		{
			LOGGER.warning("Incorrect private store type: " + data.getOptionId());
			return;
		}
		
		// Player shouldn't be able to set stores if he/she is alike dead (dead or fake death)
		if (!player.canOpenPrivateStore())
		{
			if (player.isInsideZone(ZoneId.NO_STORE))
			{
				player.sendPacket(SystemMessageId.YOU_CANNOT_OPEN_A_PRIVATE_STORE_HERE);
			}
			
			player.sendPacket(ActionFailed.STATIC_PACKET);
			return;
		}
		
		switch (type)
		{
			case SELL:
			case SELL_MANAGE:
			case PACKAGE_SELL:
			{
				if ((player.getPrivateStoreType() == PrivateStoreType.SELL) || (player.getPrivateStoreType() == PrivateStoreType.SELL_MANAGE) || (player.getPrivateStoreType() == PrivateStoreType.PACKAGE_SELL))
				{
					player.setPrivateStoreType(PrivateStoreType.NONE);
				}
				break;
			}
			case BUY:
			case BUY_MANAGE:
			{
				if ((player.getPrivateStoreType() == PrivateStoreType.BUY) || (player.getPrivateStoreType() == PrivateStoreType.BUY_MANAGE))
				{
					player.setPrivateStoreType(PrivateStoreType.NONE);
				}
				break;
			}
			case MANUFACTURE:
			{
				player.setPrivateStoreType(PrivateStoreType.NONE);
				player.broadcastUserInfo();
			}
		}
		
		if (player.getPrivateStoreType() == PrivateStoreType.NONE)
		{
			if (player.isSitting())
			{
				player.standUp();
			}
			
			switch (type)
			{
				case SELL:
				case SELL_MANAGE:
				case PACKAGE_SELL:
				{
					player.setPrivateStoreType(PrivateStoreType.SELL_MANAGE);
					player.sendPacket(new PrivateStoreManageListSell(1, player, type == PrivateStoreType.PACKAGE_SELL));
					player.sendPacket(new PrivateStoreManageListSell(2, player, type == PrivateStoreType.PACKAGE_SELL));
					break;
				}
				case BUY:
				case BUY_MANAGE:
				{
					player.setPrivateStoreType(PrivateStoreType.BUY_MANAGE);
					player.sendPacket(new PrivateStoreManageListBuy(1, player));
					player.sendPacket(new PrivateStoreManageListBuy(2, player));
					break;
				}
				case MANUFACTURE:
				{
					player.sendPacket(new RecipeShopManageList(player, true));
				}
			}
		}
	}
}
