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

import org.l2jdd.Config;
import org.l2jdd.commons.network.PacketReader;
import org.l2jdd.gameserver.model.actor.instance.PetInstance;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.model.items.instance.ItemInstance;
import org.l2jdd.gameserver.network.GameClient;
import org.l2jdd.gameserver.network.serverpackets.PetItemList;
import org.l2jdd.gameserver.util.Util;

/**
 * @version $Revision: 1.3.4.4 $ $Date: 2005/03/29 23:15:33 $
 */
public class RequestGetItemFromPet implements IClientIncomingPacket
{
	private int _objectId;
	private long _amount;
	@SuppressWarnings("unused")
	private int _unknown;
	
	@Override
	public boolean read(GameClient client, PacketReader packet)
	{
		_objectId = packet.readD();
		_amount = packet.readQ();
		_unknown = packet.readD(); // = 0 for most trades
		return true;
	}
	
	@Override
	public void run(GameClient client)
	{
		final PlayerInstance player = client.getPlayer();
		if ((_amount <= 0) || (player == null) || !player.hasPet())
		{
			return;
		}
		
		if (!client.getFloodProtectors().getTransaction().tryPerformAction("getfrompet"))
		{
			player.sendMessage("You get items from pet too fast.");
			return;
		}
		
		if (player.hasItemRequest())
		{
			return;
		}
		
		final PetInstance pet = player.getPet();
		final ItemInstance item = pet.getInventory().getItemByObjectId(_objectId);
		if (item == null)
		{
			return;
		}
		
		if (_amount > item.getCount())
		{
			Util.handleIllegalPlayerAction(player, getClass().getSimpleName() + ": Character " + player.getName() + " of account " + player.getAccountName() + " tried to get item with oid " + _objectId + " from pet but has invalid count " + _amount + " item count: " + item.getCount(), Config.DEFAULT_PUNISH);
			return;
		}
		
		final ItemInstance transferedItem = pet.transferItem("Transfer", _objectId, _amount, player.getInventory(), player, pet);
		if (transferedItem != null)
		{
			player.sendPacket(new PetItemList(pet.getInventory().getItems()));
		}
		else
		{
			LOGGER.warning("Invalid item transfer request: " + pet.getName() + "(pet) --> " + player.getName());
		}
	}
}
