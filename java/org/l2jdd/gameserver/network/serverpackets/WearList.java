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

import java.util.List;

import org.l2jdd.Config;
import org.l2jdd.commons.network.PacketWriter;
import org.l2jdd.gameserver.model.StoreTradeList;
import org.l2jdd.gameserver.model.items.Item;
import org.l2jdd.gameserver.model.items.instance.ItemInstance;
import org.l2jdd.gameserver.network.OutgoingPackets;

public class WearList implements IClientOutgoingPacket
{
	private final int _listId;
	private final ItemInstance[] _list;
	private final int _money;
	private int _expertise;
	
	public WearList(StoreTradeList list, int currentMoney, int expertiseIndex)
	{
		_listId = list.getListId();
		final List<ItemInstance> lst = list.getItems();
		_list = lst.toArray(new ItemInstance[lst.size()]);
		_money = currentMoney;
		_expertise = expertiseIndex;
	}
	
	public WearList(List<ItemInstance> lst, int listId, int currentMoney)
	{
		_listId = listId;
		_list = lst.toArray(new ItemInstance[lst.size()]);
		_money = currentMoney;
	}
	
	@Override
	public boolean write(PacketWriter packet)
	{
		OutgoingPackets.WEAR_LIST.writeId(packet);
		packet.writeC(0xc0); // ?
		packet.writeC(0x13); // ?
		packet.writeC(0x00); // ?
		packet.writeC(0x00); // ?
		packet.writeD(_money); // current money
		packet.writeD(_listId);
		
		int newlength = 0;
		for (ItemInstance item : _list)
		{
			if ((item.getItem().getCrystalType() <= _expertise) && item.isEquipable())
			{
				newlength++;
			}
		}
		packet.writeH(newlength);
		
		for (ItemInstance item : _list)
		{
			if ((item.getItem().getCrystalType() <= _expertise) && item.isEquipable())
			{
				packet.writeD(item.getItemId());
				packet.writeH(item.getItem().getType2()); // item type2
				
				if (item.getItem().getType1() != Item.TYPE1_ITEM_QUESTITEM_ADENA)
				{
					packet.writeH(item.getItem().getBodyPart()); // rev 415 slot 0006-lr.ear 0008-neck 0030-lr.finger 0040-head 0080-?? 0100-l.hand 0200-gloves 0400-chest 0800-pants 1000-feet 2000-?? 4000-r.hand 8000-r.hand
				}
				else
				{
					packet.writeH(0x00); // rev 415 slot 0006-lr.ear 0008-neck 0030-lr.finger 0040-head 0080-?? 0100-l.hand 0200-gloves 0400-chest 0800-pants 1000-feet 2000-?? 4000-r.hand 8000-r.hand
				}
				
				packet.writeD(Config.WEAR_PRICE);
			}
		}
		return true;
	}
}
