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
import org.l2jdd.gameserver.enums.ChatType;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.network.GameClient;
import org.l2jdd.gameserver.network.serverpackets.ActionFailed;
import org.l2jdd.gameserver.network.serverpackets.CreatureSay;
import org.l2jdd.gameserver.network.serverpackets.PrivateStoreManageListBuy;
import org.l2jdd.gameserver.util.Util;

public class RequestPrivateStoreManageBuy implements IClientIncomingPacket
{
	@Override
	public boolean read(GameClient client, PacketReader packet)
	{
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
		
		// Fix for privatestore exploit during login
		if (!player.isSpawned() || player.isLocked())
		{
			Util.handleIllegalPlayerAction(player, "Player " + player.getName() + " try exploit at login with privatestore!", Config.DEFAULT_PUNISH);
			LOGGER.warning("Player " + player.getName() + " try exploit at login with privatestore!");
			return;
		}
		
		// Private store disabled by config
		if (player.isGM() && Config.GM_TRADE_RESTRICTED_ITEMS)
		{
			player.sendMessage("Gm private store disabled by config!");
			player.sendPacket(ActionFailed.STATIC_PACKET);
			return;
		}
		
		// If player is in store mode /offline_shop like L2OFF
		if (player.isStored())
		{
			player.sendPacket(ActionFailed.STATIC_PACKET);
			return;
		}
		
		// Player shouldn't be able to set stores if he/she is alike dead (dead or fake death)
		if (player.isAlikeDead())
		{
			player.sendPacket(ActionFailed.STATIC_PACKET);
			return;
		}
		
		if (player.isInOlympiadMode())
		{
			player.sendPacket(ActionFailed.STATIC_PACKET);
			return;
		}
		
		// Like L2OFF - You can't open buy/sell when you are sitting
		if (player.isSitting() && (player.getPrivateStoreType() == 0))
		{
			player.sendPacket(ActionFailed.STATIC_PACKET);
			return;
		}
		
		if (player.isSitting() && (player.getPrivateStoreType() != 0))
		{
			player.standUp();
		}
		
		if (player.getMountType() != 0)
		{
			return;
		}
		
		if ((player.getPrivateStoreType() == PlayerInstance.STORE_PRIVATE_BUY) || (player.getPrivateStoreType() == (PlayerInstance.STORE_PRIVATE_BUY + 1)))
		{
			player.setPrivateStoreType(PlayerInstance.STORE_PRIVATE_NONE);
		}
		
		if (player.getPrivateStoreType() == PlayerInstance.STORE_PRIVATE_NONE)
		{
			if (player.isSitting())
			{
				player.standUp();
			}
			
			if (Config.SELL_BY_ITEM)
			{
				player.sendPacket(new CreatureSay(0, ChatType.PARTYROOM_COMMANDER, "", "ATTENTION: Store System is not based on Adena, be careful!"));
			}
			
			player.setPrivateStoreType(PlayerInstance.STORE_PRIVATE_BUY + 1);
			player.sendPacket(new PrivateStoreManageListBuy(player));
		}
	}
}