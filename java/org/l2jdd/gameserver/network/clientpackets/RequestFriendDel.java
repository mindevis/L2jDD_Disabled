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

import java.sql.Connection;
import java.sql.PreparedStatement;

import org.l2jdd.commons.database.DatabaseFactory;
import org.l2jdd.commons.network.PacketReader;
import org.l2jdd.gameserver.data.sql.CharNameTable;
import org.l2jdd.gameserver.model.World;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.network.GameClient;
import org.l2jdd.gameserver.network.SystemMessageId;
import org.l2jdd.gameserver.network.serverpackets.FriendList;
import org.l2jdd.gameserver.network.serverpackets.SystemMessage;

public class RequestFriendDel implements IClientIncomingPacket
{
	private String _name;
	
	@Override
	public boolean read(GameClient client, PacketReader packet)
	{
		_name = packet.readS();
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
		
		final int id = CharNameTable.getInstance().getPlayerObjectId(_name);
		if ((id <= 0) || !player.getFriendList().contains(id))
		{
			player.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.S1_IS_NOT_ON_YOUR_FRIEND_LIST).addString(_name));
			return;
		}
		
		try (Connection con = DatabaseFactory.getConnection())
		{
			final PreparedStatement statement = con.prepareStatement("DELETE FROM character_friends WHERE (char_id = ? AND friend_id = ?) OR (char_id = ? AND friend_id = ?)");
			statement.setInt(1, player.getObjectId());
			statement.setInt(2, id);
			statement.setInt(3, id);
			statement.setInt(4, player.getObjectId());
			statement.execute();
			statement.close();
			
			// Player deleted from your friendlist
			player.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.S1_HAS_BEEN_DELETED_FROM_YOUR_FRIENDS_LIST).addString(_name));
			
			player.getFriendList().remove(Integer.valueOf(id));
			player.sendPacket(new FriendList(player)); // update friendList *heavy method*
			
			final PlayerInstance target = World.getInstance().getPlayer(_name);
			if (target != null)
			{
				target.getFriendList().remove(Integer.valueOf(player.getObjectId()));
				target.sendPacket(new FriendList(target)); // update friendList *heavy method*
			}
		}
		catch (Exception e)
		{
			LOGGER.warning("could not delete friend objectid: " + e);
		}
	}
}