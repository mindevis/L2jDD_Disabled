/*
 * This file is part of the L2J Mobius project.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.l2jdd.gameserver.network.clientpackets;

import java.util.List;

import org.l2jdd.Config;
import org.l2jdd.commons.network.PacketReader;
import org.l2jdd.gameserver.TradeController;
import org.l2jdd.gameserver.cache.HtmCache;
import org.l2jdd.gameserver.data.ItemTable;
import org.l2jdd.gameserver.model.StoreTradeList;
import org.l2jdd.gameserver.model.WorldObject;
import org.l2jdd.gameserver.model.actor.instance.CastleChamberlainInstance;
import org.l2jdd.gameserver.model.actor.instance.ClanHallManagerInstance;
import org.l2jdd.gameserver.model.actor.instance.FishermanInstance;
import org.l2jdd.gameserver.model.actor.instance.MercManagerInstance;
import org.l2jdd.gameserver.model.actor.instance.MerchantInstance;
import org.l2jdd.gameserver.model.actor.instance.NpcInstance;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.model.items.Item;
import org.l2jdd.gameserver.network.GameClient;
import org.l2jdd.gameserver.network.SystemMessageId;
import org.l2jdd.gameserver.network.serverpackets.ActionFailed;
import org.l2jdd.gameserver.network.serverpackets.ItemList;
import org.l2jdd.gameserver.network.serverpackets.NpcHtmlMessage;
import org.l2jdd.gameserver.network.serverpackets.StatusUpdate;
import org.l2jdd.gameserver.network.serverpackets.SystemMessage;
import org.l2jdd.gameserver.util.Util;

/**
 * @version $Revision: 1.12.4.4 $ $Date: 2005/03/27 15:29:30 $
 */
public class RequestBuyItem implements IClientIncomingPacket
{
	private int _listId;
	private int _count;
	private int[] _items; // count*2
	
	@Override
	public boolean read(GameClient client, PacketReader packet)
	{
		_listId = packet.readD();
		_count = packet.readD();
		// count*8 is the size of a for iteration of each item
		if (((_count * 2) < 0) || (_count > Config.MAX_ITEM_IN_PACKET) || ((_count * 8) > packet.getReadableBytes()))
		{
			_count = 0;
		}
		
		_items = new int[_count * 2];
		for (int i = 0; i < _count; i++)
		{
			final int itemId = packet.readD();
			if (itemId < 1)
			{
				_count = 0;
				return false;
			}
			_items[(i * 2) + 0] = itemId;
			
			final int count = packet.readD();
			if ((count > Integer.MAX_VALUE) || (count < 1))
			{
				_count = 0;
				return false;
			}
			if (count > 10000) // Count check.
			{
				client.getPlayer().sendMessage("You cannot buy more than 10.000 items.");
				_count = 0;
				return false;
			}
			_items[(i * 2) + 1] = count;
		}
		
		return true;
	}
	
	@Override
	public void run(GameClient client)
	{
		final PlayerInstance player = client.getPlayer();
		if (player == null)
		{
			return;
		}
		
		if (!client.getFloodProtectors().getTransaction().tryPerformAction("buy"))
		{
			player.sendMessage("You buying too fast.");
			return;
		}
		
		// Alt game - Karma punishment
		if (!Config.ALT_GAME_KARMA_PLAYER_CAN_SHOP && (player.getKarma() > 0))
		{
			return;
		}
		
		final WorldObject target = player.getTarget();
		if (!player.isGM() && ((target == null // No target (ie GM Shop)
		) || (!(target instanceof MerchantInstance) && !(target instanceof FishermanInstance) && !(target instanceof MercManagerInstance) && !(target instanceof ClanHallManagerInstance) && !(target instanceof CastleChamberlainInstance)) // Target not a merchant, fisherman or mercmanager
			|| !player.isInsideRadius2D(target, NpcInstance.INTERACTION_DISTANCE) // Distance is too far
		))
		{
			return;
		}
		
		boolean ok = true;
		String htmlFolder = "";
		if (target != null)
		{
			if (target instanceof MerchantInstance)
			{
				htmlFolder = "merchant";
			}
			else if (target instanceof FishermanInstance)
			{
				htmlFolder = "fisherman";
			}
			else if (target instanceof MercManagerInstance)
			{
				ok = true;
			}
			else if (target instanceof ClanHallManagerInstance)
			{
				ok = true;
			}
			else if (target instanceof CastleChamberlainInstance)
			{
				ok = true;
			}
			else
			{
				ok = false;
			}
		}
		else
		{
			ok = false;
		}
		
		NpcInstance merchant = null;
		if (ok)
		{
			merchant = (NpcInstance) target;
		}
		else if (!ok && !player.isGM())
		{
			player.sendMessage("Invalid Target: Seller must be targetted");
			return;
		}
		
		StoreTradeList list = null;
		if (merchant != null)
		{
			final List<StoreTradeList> lists = TradeController.getInstance().getBuyListByNpcId(merchant.getNpcId());
			if (!player.isGM())
			{
				if (lists == null)
				{
					Util.handleIllegalPlayerAction(player, "Warning!! Character " + player.getName() + " of account " + player.getAccountName() + " sent a false BuyList list_id.", Config.DEFAULT_PUNISH);
					return;
				}
				for (StoreTradeList tradeList : lists)
				{
					if (tradeList.getListId() == _listId)
					{
						list = tradeList;
					}
				}
			}
			else
			{
				list = TradeController.getInstance().getBuyList(_listId);
			}
		}
		else
		{
			list = TradeController.getInstance().getBuyList(_listId);
		}
		
		if (list == null)
		{
			Util.handleIllegalPlayerAction(player, "Warning!! Character " + player.getName() + " of account " + player.getAccountName() + " sent a false BuyList list_id.", Config.DEFAULT_PUNISH);
			return;
		}
		
		_listId = list.getListId();
		if ((_listId > 1000000) && (merchant != null) && (merchant.getTemplate().getNpcId() != (_listId - 1000000)))
		{
			player.sendPacket(ActionFailed.STATIC_PACKET);
			return;
		}
		
		if (_count < 1)
		{
			player.sendPacket(ActionFailed.STATIC_PACKET);
			return;
		}
		
		double taxRate = 0;
		if ((merchant != null) && merchant.isInTown())
		{
			taxRate = merchant.getCastle().getTaxRate();
		}
		
		long subTotal = 0;
		int tax = 0;
		
		// Check for buylist validity and calculates summary values
		long slots = 0;
		long weight = 0;
		for (int i = 0; i < _count; i++)
		{
			final int itemId = _items[(i * 2) + 0];
			final int count = _items[(i * 2) + 1];
			int price = -1;
			if (!list.containsItemId(itemId))
			{
				Util.handleIllegalPlayerAction(player, "Warning!! Character " + player.getName() + " of account " + player.getAccountName() + " sent a false BuyList list_id.", Config.DEFAULT_PUNISH);
				return;
			}
			
			final Item template = ItemTable.getInstance().getTemplate(itemId);
			if (template == null)
			{
				continue;
			}
			
			// Check count
			if ((count > Integer.MAX_VALUE) || (!template.isStackable() && (count > 1)))
			{
				// Util.handleIllegalPlayerAction(player, "Warning!! Character " + player.getName() + " of account " + player.getAccountName() + " tried to purchase invalid quantity of items at the same time.", Config.DEFAULT_PUNISH);
				player.sendPacket(new SystemMessage(SystemMessageId.YOU_HAVE_EXCEEDED_THE_QUANTITY_THAT_CAN_BE_INPUTTED));
				return;
			}
			
			if (_listId < 1000000)
			{
				// list = TradeController.getInstance().getBuyList(_listId);
				price = list.getPriceForItemId(itemId);
				if ((itemId >= 3960) && (itemId <= 4026))
				{
					price *= Config.RATE_SIEGE_GUARDS_PRICE;
				}
			}
			
			if (price < 0)
			{
				LOGGER.warning("ERROR, no price found .. wrong buylist ??");
				player.sendPacket(ActionFailed.STATIC_PACKET);
				return;
			}
			
			if ((price == 0) && !player.isGM() && Config.ONLY_GM_ITEMS_FREE)
			{
				player.sendMessage("Ohh Cheat dont work? You have a problem now!");
				Util.handleIllegalPlayerAction(player, "Warning!! Character " + player.getName() + " of account " + player.getAccountName() + " tried buy item for 0 adena.", Config.DEFAULT_PUNISH);
				return;
			}
			
			subTotal += count * price; // Before tax
			tax = (int) (subTotal * taxRate);
			
			// Check subTotal + tax
			if ((subTotal + tax) > Integer.MAX_VALUE)
			{
				// Util.handleIllegalPlayerAction(player, "Warning!! Character " + player.getName() + " of account " + player.getAccountName() + " tried to purchase over " + Integer.MAX_VALUE + " adena worth of goods.", Config.DEFAULT_PUNISH);
				player.sendPacket(new SystemMessage(SystemMessageId.YOU_HAVE_EXCEEDED_THE_QUANTITY_THAT_CAN_BE_INPUTTED));
				return;
			}
			
			weight += count * template.getWeight();
			if (!template.isStackable())
			{
				slots += count;
			}
			else if (player.getInventory().getItemByItemId(itemId) == null)
			{
				slots++;
			}
		}
		
		if ((weight > Integer.MAX_VALUE) || (weight < 0) || !player.getInventory().validateWeight((int) weight))
		{
			player.sendPacket(SystemMessageId.YOU_HAVE_EXCEEDED_THE_WEIGHT_LIMIT);
			return;
		}
		
		if ((slots > Integer.MAX_VALUE) || (slots < 0) || !player.getInventory().validateCapacity((int) slots))
		{
			player.sendPacket(SystemMessageId.YOUR_INVENTORY_IS_FULL);
			return;
		}
		
		// Charge buyer and add tax to castle treasury if not owned by npc clan
		if ((subTotal < 0) || !player.reduceAdena("Buy", (int) (subTotal + tax), player.getLastFolkNPC(), false))
		{
			player.sendPacket(SystemMessageId.YOU_DO_NOT_HAVE_ENOUGH_ADENA);
			return;
		}
		
		if ((merchant != null) && merchant.isInTown() && (merchant.getCastle().getOwnerId() > 0))
		{
			merchant.getCastle().addToTreasury(tax);
		}
		
		// Proceed the purchase
		for (int i = 0; i < _count; i++)
		{
			final int itemId = _items[(i * 2) + 0];
			int count = _items[(i * 2) + 1];
			if (count < 0)
			{
				count = 0;
			}
			
			if (!list.containsItemId(itemId))
			{
				Util.handleIllegalPlayerAction(player, "Warning!! Character " + player.getName() + " of account " + player.getAccountName() + " sent a false BuyList list_id.", Config.DEFAULT_PUNISH);
				return;
			}
			
			if (list.countDecrease(itemId) && !list.decreaseCount(itemId, count))
			{
				player.sendPacket(new SystemMessage(SystemMessageId.YOU_HAVE_EXCEEDED_THE_QUANTITY_THAT_CAN_BE_INPUTTED));
				return;
			}
			// Add item to Inventory and adjust update packet
			player.getInventory().addItem("Buy", itemId, count, player, merchant);
		}
		
		if (merchant != null)
		{
			final String html = HtmCache.getInstance().getHtm("data/html/" + htmlFolder + "/" + merchant.getNpcId() + "-bought.htm");
			if (html != null)
			{
				final NpcHtmlMessage boughtMsg = new NpcHtmlMessage(merchant.getObjectId());
				boughtMsg.setHtml(html.replace("%objectId%", String.valueOf(merchant.getObjectId())));
				player.sendPacket(boughtMsg);
			}
		}
		
		final StatusUpdate su = new StatusUpdate(player.getObjectId());
		su.addAttribute(StatusUpdate.CUR_LOAD, player.getCurrentLoad());
		player.sendPacket(su);
		player.sendPacket(new ItemList(player, true));
	}
}
