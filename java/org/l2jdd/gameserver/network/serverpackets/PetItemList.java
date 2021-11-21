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
import org.l2jdd.gameserver.model.actor.instance.PetInstance;
import org.l2jdd.gameserver.model.items.instance.ItemInstance;
import org.l2jdd.gameserver.network.OutgoingPackets;

/**
 * @version $Revision: 1.4.2.1.2.4 $ $Date: 2005/03/27 15:29:39 $
 */
public class PetItemList implements IClientOutgoingPacket
{
	private final PetInstance _activeChar;
	
	public PetItemList(PetInstance character)
	{
		_activeChar = character;
	}
	
	@Override
	public boolean write(PacketWriter packet)
	{
		OutgoingPackets.PET_ITEM_LIST.writeId(packet);
		
		final ItemInstance[] items = _activeChar.getInventory().getItems();
		final int count = items.length;
		packet.writeH(count);
		
		for (ItemInstance temp : items)
		{
			packet.writeH(temp.getItem().getType1()); // item type1
			packet.writeD(temp.getObjectId());
			packet.writeD(temp.getItemId());
			packet.writeD(temp.getCount());
			packet.writeH(temp.getItem().getType2()); // item type2
			packet.writeH(0xff); // ?
			if (temp.isEquipped())
			{
				packet.writeH(0x01);
			}
			else
			{
				packet.writeH(0x00);
			}
			packet.writeD(temp.getItem().getBodyPart()); // rev 415 slot 0006-lr.ear 0008-neck 0030-lr.finger 0040-head 0080-?? 0100-l.hand 0200-gloves 0400-chest 0800-pants 1000-feet 2000-?? 4000-r.hand 8000-r.hand
			// writeH(temp.getItem().getBodyPart()); // rev 377 slot 0006-lr.ear 0008-neck 0030-lr.finger 0040-head 0080-?? 0100-l.hand 0200-gloves 0400-chest 0800-pants 1000-feet 2000-?? 4000-r.hand 8000-r.hand
			packet.writeH(temp.getEnchantLevel()); // enchant level
			packet.writeH(0x00); // ?
		}
		return true;
	}
}
