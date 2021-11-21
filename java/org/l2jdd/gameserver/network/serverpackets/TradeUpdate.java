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
package org.l2jdd.gameserver.network.serverpackets;

import org.l2jdd.commons.network.PacketWriter;
import org.l2jdd.gameserver.model.TradeList;
import org.l2jdd.gameserver.model.TradeList.TradeItem;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.model.items.instance.ItemInstance;
import org.l2jdd.gameserver.network.OutgoingPackets;

/**
 * @author Beetle
 */
public class TradeUpdate implements IClientOutgoingPacket
{
	private final ItemInstance[] _items;
	private final TradeItem[] _tradeItems;
	
	public TradeUpdate(TradeList trade, PlayerInstance player)
	{
		_items = player.getInventory().getItems();
		_tradeItems = trade.getItems();
	}
	
	private int getItemCount(int objectId)
	{
		for (ItemInstance item : _items)
		{
			if (item.getObjectId() == objectId)
			{
				return item.getCount();
			}
		}
		return 0;
	}
	
	@Override
	public boolean write(PacketWriter packet)
	{
		OutgoingPackets.TRADE_UPDATE.writeId(packet);
		
		packet.writeH(_tradeItems.length);
		for (TradeItem item : _tradeItems)
		{
			int aveCount = getItemCount(item.getObjectId()) - item.getCount();
			boolean stackable = item.getItem().isStackable();
			if (aveCount == 0)
			{
				aveCount = 1;
				stackable = false;
			}
			packet.writeH(stackable ? 3 : 2);
			packet.writeH(item.getItem().getType1()); // item type1
			packet.writeD(item.getObjectId());
			packet.writeD(item.getItem().getItemId());
			packet.writeD(aveCount);
			packet.writeH(item.getItem().getType2()); // item type2
			packet.writeH(0x00); // ?
			packet.writeD(item.getItem().getBodyPart()); // rev 415 slot 0006-lr.ear 0008-neck 0030-lr.finger 0040-head 0080-?? 0100-l.hand 0200-gloves 0400-chest 0800-pants 1000-feet 2000-?? 4000-r.hand 8000-r.hand
			packet.writeH(item.getEnchant()); // enchant level
			packet.writeH(0x00); // ?
			packet.writeH(0x00);
		}
		return true;
	}
}