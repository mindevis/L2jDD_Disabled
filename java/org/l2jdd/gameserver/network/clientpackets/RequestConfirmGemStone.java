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

import org.l2jdd.commons.network.PacketReader;
import org.l2jdd.gameserver.model.World;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.model.items.Item;
import org.l2jdd.gameserver.model.items.instance.ItemInstance;
import org.l2jdd.gameserver.network.GameClient;
import org.l2jdd.gameserver.network.SystemMessageId;
import org.l2jdd.gameserver.network.serverpackets.ExConfirmVariationGemstone;

/**
 * Format:(ch) dddd
 * @author -Wooden-
 */
public class RequestConfirmGemStone implements IClientIncomingPacket
{
	private int _targetItemObjId;
	private int _refinerItemObjId;
	private int _gemstoneItemObjId;
	private int _gemstoneCount;
	
	@Override
	public boolean read(GameClient client, PacketReader packet)
	{
		_targetItemObjId = packet.readD();
		_refinerItemObjId = packet.readD();
		_gemstoneItemObjId = packet.readD();
		_gemstoneCount = packet.readD();
		return true;
	}
	
	@Override
	public void run(GameClient client)
	{
		final PlayerInstance player = client.getPlayer();
		final ItemInstance targetItem = (ItemInstance) World.getInstance().findObject(_targetItemObjId);
		final ItemInstance refinerItem = (ItemInstance) World.getInstance().findObject(_refinerItemObjId);
		final ItemInstance gemstoneItem = (ItemInstance) World.getInstance().findObject(_gemstoneItemObjId);
		if ((targetItem == null) || (refinerItem == null) || (gemstoneItem == null))
		{
			return;
		}
		
		// Make sure the item is a gemstone
		final int gemstoneItemId = gemstoneItem.getItem().getItemId();
		if ((gemstoneItemId != 2130) && (gemstoneItemId != 2131))
		{
			player.sendPacket(SystemMessageId.THIS_IS_NOT_A_SUITABLE_ITEM);
			return;
		}
		
		// Check if the gemstoneCount is sufficant
		final int itemGrade = targetItem.getItem().getItemGrade();
		
		switch (itemGrade)
		{
			case Item.CRYSTAL_C:
			{
				if ((_gemstoneCount != 20) || (gemstoneItemId != 2130))
				{
					player.sendPacket(SystemMessageId.GEMSTONE_QUANTITY_IS_INCORRECT);
					return;
				}
				break;
			}
			case Item.CRYSTAL_B:
			{
				if ((_gemstoneCount != 30) || (gemstoneItemId != 2130))
				{
					player.sendPacket(SystemMessageId.GEMSTONE_QUANTITY_IS_INCORRECT);
					return;
				}
				break;
			}
			case Item.CRYSTAL_A:
			{
				if ((_gemstoneCount != 20) || (gemstoneItemId != 2131))
				{
					player.sendPacket(SystemMessageId.GEMSTONE_QUANTITY_IS_INCORRECT);
					return;
				}
				break;
			}
			case Item.CRYSTAL_S:
			{
				if ((_gemstoneCount != 25) || (gemstoneItemId != 2131))
				{
					player.sendPacket(SystemMessageId.GEMSTONE_QUANTITY_IS_INCORRECT);
					return;
				}
				break;
			}
		}
		
		player.sendPacket(new ExConfirmVariationGemstone(_gemstoneItemObjId, _gemstoneCount));
		player.sendPacket(SystemMessageId.PRESS_THE_AUGMENT_BUTTON_TO_BEGIN);
	}
}