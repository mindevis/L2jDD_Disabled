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
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.model.partymatching.PartyMatchRoom;
import org.l2jdd.gameserver.model.partymatching.PartyMatchRoomList;
import org.l2jdd.gameserver.model.partymatching.PartyMatchWaitingList;
import org.l2jdd.gameserver.network.GameClient;
import org.l2jdd.gameserver.network.SystemMessageId;
import org.l2jdd.gameserver.network.serverpackets.ActionFailed;
import org.l2jdd.gameserver.network.serverpackets.ExPartyRoomMember;
import org.l2jdd.gameserver.network.serverpackets.PartyMatchDetail;
import org.l2jdd.gameserver.network.serverpackets.PartyMatchList;

public class RequestPartyMatchConfig implements IClientIncomingPacket
{
	private int _auto;
	private int _loc;
	private int _level;
	
	@Override
	public boolean read(GameClient client, PacketReader packet)
	{
		_auto = packet.readD();
		_loc = packet.readD();
		_level = packet.readD();
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
		
		if (!player.isInPartyMatchRoom() && (player.getParty() != null) && (player.getParty().getLeader() != player))
		{
			player.sendPacket(SystemMessageId.THE_LIST_OF_PARTY_ROOMS_CAN_ONLY_BE_VIEWED_BY_A_PERSON_WHO_HAS_NOT_JOINED_A_PARTY_OR_WHO_IS_CURRENTLY_THE_LEADER_OF_A_PARTY);
			player.sendPacket(ActionFailed.STATIC_PACKET);
			return;
		}
		
		if (player.isInPartyMatchRoom())
		{
			// If Player is in Room show him room, not list
			final PartyMatchRoomList list = PartyMatchRoomList.getInstance();
			if (list == null)
			{
				return;
			}
			
			final PartyMatchRoom room = list.getPlayerRoom(player);
			if (room == null)
			{
				return;
			}
			
			player.sendPacket(new PartyMatchDetail(room));
			player.sendPacket(new ExPartyRoomMember(room, 2));
			player.setPartyRoom(room.getId());
			player.broadcastUserInfo();
		}
		else
		{
			// Add to waiting list
			PartyMatchWaitingList.getInstance().addPlayer(player);
			
			// Send Room list
			player.sendPacket(new PartyMatchList(player, _auto, _loc, _level));
		}
	}
}