
package handlers.bypasshandlers;

import java.util.logging.Level;

import org.l2jdd.Config;
import org.l2jdd.gameserver.handler.IBypassHandler;
import org.l2jdd.gameserver.model.actor.Creature;
import org.l2jdd.gameserver.model.actor.Npc;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.model.actor.instance.WarehouseInstance;
import org.l2jdd.gameserver.model.clan.ClanPrivilege;
import org.l2jdd.gameserver.model.items.instance.ItemInstance;
import org.l2jdd.gameserver.network.SystemMessageId;
import org.l2jdd.gameserver.network.serverpackets.ActionFailed;
import org.l2jdd.gameserver.network.serverpackets.WareHouseDepositList;
import org.l2jdd.gameserver.network.serverpackets.WareHouseWithdrawalList;

public class ClanWarehouse implements IBypassHandler
{
	private static final String[] COMMANDS =
	{
		"withdrawc",
		"depositc"
	};
	
	@Override
	public boolean useBypass(String command, PlayerInstance player, Creature target)
	{
		if (!Config.ALLOW_WAREHOUSE)
		{
			return false;
		}
		
		if (!target.isNpc())
		{
			return false;
		}
		
		final Npc npc = (Npc) target;
		if (!(npc instanceof WarehouseInstance) && (npc.getClan() != null))
		{
			return false;
		}
		
		if (player.hasItemRequest())
		{
			return false;
		}
		else if (player.getClan() == null)
		{
			player.sendPacket(SystemMessageId.YOU_DO_NOT_HAVE_THE_RIGHT_TO_USE_THE_CLAN_WAREHOUSE);
			return false;
		}
		else if (player.getClan().getLevel() == 0)
		{
			player.sendPacket(SystemMessageId.ONLY_CLANS_OF_CLAN_LEVEL_1_OR_ABOVE_CAN_USE_A_CLAN_WAREHOUSE);
			return false;
		}
		else
		{
			try
			{
				if (command.toLowerCase().startsWith(COMMANDS[0])) // WithdrawC
				{
					player.sendPacket(ActionFailed.STATIC_PACKET);
					
					if (!player.hasClanPrivilege(ClanPrivilege.CL_VIEW_WAREHOUSE))
					{
						player.sendPacket(SystemMessageId.YOU_DO_NOT_HAVE_THE_RIGHT_TO_USE_THE_CLAN_WAREHOUSE);
						return true;
					}
					
					player.setActiveWarehouse(player.getClan().getWarehouse());
					
					if (player.getActiveWarehouse().getSize() == 0)
					{
						player.sendPacket(SystemMessageId.YOU_HAVE_NOT_DEPOSITED_ANY_ITEMS_IN_YOUR_WAREHOUSE);
						return true;
					}
					
					for (ItemInstance i : player.getActiveWarehouse().getItems())
					{
						if (i.isTimeLimitedItem() && (i.getRemainingTime() <= 0))
						{
							player.getActiveWarehouse().destroyItem("ItemInstance", i, player, null);
						}
					}
					
					player.sendPacket(new WareHouseWithdrawalList(1, player, WareHouseWithdrawalList.CLAN));
					player.sendPacket(new WareHouseWithdrawalList(2, player, WareHouseWithdrawalList.CLAN));
					return true;
				}
				else if (command.toLowerCase().startsWith(COMMANDS[1])) // DepositC
				{
					player.sendPacket(ActionFailed.STATIC_PACKET);
					player.setActiveWarehouse(player.getClan().getWarehouse());
					player.setInventoryBlockingStatus(true);
					player.sendPacket(new WareHouseDepositList(1, player, WareHouseDepositList.CLAN));
					player.sendPacket(new WareHouseDepositList(2, player, WareHouseDepositList.CLAN));
					return true;
				}
				
				return false;
			}
			catch (Exception e)
			{
				LOGGER.log(Level.WARNING, "Exception in " + getClass().getSimpleName(), e);
			}
		}
		return false;
	}
	
	@Override
	public String[] getBypassList()
	{
		return COMMANDS;
	}
}
