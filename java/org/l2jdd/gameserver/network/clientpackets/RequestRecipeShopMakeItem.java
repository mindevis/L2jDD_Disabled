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
import org.l2jdd.gameserver.RecipeController;
import org.l2jdd.gameserver.model.World;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.network.GameClient;
import org.l2jdd.gameserver.network.SystemMessageId;
import org.l2jdd.gameserver.util.Util;

public class RequestRecipeShopMakeItem implements IClientIncomingPacket
{
	private int _id;
	private int _recipeId;
	@SuppressWarnings("unused")
	private int _unknow;
	
	@Override
	public boolean read(GameClient client, PacketReader packet)
	{
		_id = packet.readD();
		_recipeId = packet.readD();
		_unknow = packet.readD();
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
		
		if (!client.getFloodProtectors().getManufacture().tryPerformAction("RecipeShopMake"))
		{
			return;
		}
		
		final PlayerInstance manufacturer = World.getInstance().getPlayer(_id);
		if (manufacturer == null)
		{
			return;
		}
		
		if (player.getPrivateStoreType() != 0)
		{
			// player.sendMessage("Cannot create items while trading.");
			return;
		}
		
		if (manufacturer.getPrivateStoreType() != 5)
		{
			// player.sendMessage("Cannot make items while trading");
			return;
		}
		
		if (player.isCrafting() || manufacturer.isCrafting())
		{
			// player.sendMessage("Currently in Craft Mode.");
			return;
		}
		
		if (manufacturer.isInDuel() || player.isInDuel())
		{
			player.sendPacket(SystemMessageId.WHILE_YOU_ARE_ENGAGED_IN_COMBAT_YOU_CANNOT_OPERATE_A_PRIVATE_STORE_OR_PRIVATE_WORKSHOP);
			return;
		}
		
		if (Util.checkIfInRange(150, player, manufacturer, true))
		{
			RecipeController.getInstance().requestManufactureItem(manufacturer, _recipeId, player);
		}
	}
}
