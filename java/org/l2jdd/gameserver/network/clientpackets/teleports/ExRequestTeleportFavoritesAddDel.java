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
package org.l2jdd.gameserver.network.clientpackets.teleports;

import java.util.ArrayList;
import java.util.List;

import org.l2jdd.commons.network.PacketReader;
import org.l2jdd.gameserver.data.xml.TeleportListData;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.model.variables.PlayerVariables;
import org.l2jdd.gameserver.network.GameClient;
import org.l2jdd.gameserver.network.clientpackets.IClientIncomingPacket;

/**
 * @author Mobius
 */
public class ExRequestTeleportFavoritesAddDel implements IClientIncomingPacket
{
	private boolean _enable;
	private int _teleportId;
	
	@Override
	public boolean read(GameClient client, PacketReader packet)
	{
		_enable = packet.readC() == 1;
		_teleportId = packet.readD();
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
		
		if (TeleportListData.getInstance().getTeleport(_teleportId) == null)
		{
			LOGGER.warning("No registered teleport location for id: " + _teleportId);
			return;
		}
		
		final List<Integer> favorites = new ArrayList<>();
		if (player.getVariables().contains(PlayerVariables.FAVORITE_TELEPORTS))
		{
			for (int id : player.getVariables().getIntArray(PlayerVariables.FAVORITE_TELEPORTS, ","))
			{
				if (TeleportListData.getInstance().getTeleport(_teleportId) == null)
				{
					LOGGER.warning("No registered teleport location for id: " + _teleportId);
				}
				else
				{
					favorites.add(id);
				}
			}
		}
		
		if (_enable)
		{
			if (!favorites.contains(_teleportId))
			{
				favorites.add(_teleportId);
			}
		}
		else
		{
			favorites.remove((Integer) _teleportId);
		}
		
		String variable = "";
		for (int id : favorites)
		{
			variable += id + ",";
		}
		if (variable.isEmpty())
		{
			player.getVariables().remove(PlayerVariables.FAVORITE_TELEPORTS);
		}
		else
		{
			player.getVariables().set(PlayerVariables.FAVORITE_TELEPORTS, variable);
		}
	}
}
