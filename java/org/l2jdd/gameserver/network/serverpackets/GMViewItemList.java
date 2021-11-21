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
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.model.items.instance.ItemInstance;
import org.l2jdd.gameserver.network.OutgoingPackets;

/**
 * @version $Revision: 1.1.2.1.2.3 $ $Date: 2005/03/27 15:29:57 $
 */
public class GMViewItemList implements IClientOutgoingPacket
{
	private final ItemInstance[] _items;
	private final PlayerInstance _player;
	private final String _playerName;
	
	public GMViewItemList(PlayerInstance player)
	{
		_items = player.getInventory().getItems();
		_playerName = player.getName();
		_player = player;
	}
	
	@Override
	public boolean write(PacketWriter packet)
	{
		OutgoingPackets.GM_VIEW_ITEM_LIST.writeId(packet);
		packet.writeS(_playerName);
		packet.writeD(_player.getInventoryLimit()); // inventory limit
		packet.writeH(0x01); // show window ??
		packet.writeH(_items.length);
		
		for (ItemInstance temp : _items)
		{
			if ((temp == null) || (temp.getItem() == null))
			{
				continue;
			}
			
			packet.writeH(temp.getItem().getType1());
			
			packet.writeD(temp.getObjectId());
			packet.writeD(temp.getItemId());
			packet.writeD(temp.getCount());
			packet.writeH(temp.getItem().getType2());
			packet.writeH(temp.getCustomType1());
			packet.writeH(temp.isEquipped() ? 0x01 : 0x00);
			packet.writeD(temp.getItem().getBodyPart());
			packet.writeH(temp.getEnchantLevel());
			packet.writeH(temp.getCustomType2());
			if (temp.isAugmented())
			{
				packet.writeD(temp.getAugmentation().getAugmentationId());
			}
			else
			{
				packet.writeD(0x00);
			}
			packet.writeD(-1); // C6
		}
		return true;
	}
}
