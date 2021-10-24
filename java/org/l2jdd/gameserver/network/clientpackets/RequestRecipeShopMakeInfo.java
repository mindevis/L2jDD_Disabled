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
import org.l2jdd.gameserver.data.xml.RecipeData;
import org.l2jdd.gameserver.enums.PrivateStoreType;
import org.l2jdd.gameserver.model.World;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.model.holders.RecipeHolder;
import org.l2jdd.gameserver.network.GameClient;
import org.l2jdd.gameserver.network.SystemMessageId;
import org.l2jdd.gameserver.network.serverpackets.RecipeShopItemInfo;

/**
 * @version $Revision: 1.1.2.1.2.2 $ $Date: 2005/03/27 15:29:30 $
 */
public class RequestRecipeShopMakeInfo implements IClientIncomingPacket
{
	private int _playerObjectId;
	private int _recipeId;
	
	@Override
	public boolean read(GameClient client, PacketReader packet)
	{
		_playerObjectId = packet.readD();
		_recipeId = packet.readD();
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
		
		final PlayerInstance manufacturer = World.getInstance().getPlayer(_playerObjectId);
		if ((manufacturer == null) || (manufacturer.getPrivateStoreType() != PrivateStoreType.MANUFACTURE))
		{
			return;
		}
		
		final RecipeHolder recipe = RecipeData.getInstance().getRecipe(_recipeId);
		if (recipe == null)
		{
			player.sendPacket(SystemMessageId.THE_RECIPE_IS_INCORRECT);
			return;
		}
		
		final Long manufactureRecipeCost = manufacturer.getManufactureItems().get(_recipeId);
		if (manufactureRecipeCost == null)
		{
			return;
		}
		
		client.sendPacket(new RecipeShopItemInfo(manufacturer, _recipeId, manufactureRecipeCost, recipe.getMaxOffering()));
	}
}
