
package org.l2jdd.gameserver.network.clientpackets;

import org.l2jdd.commons.network.PacketReader;
import org.l2jdd.gameserver.instancemanager.MatchingRoomManager;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.model.matching.MatchingRoom;
import org.l2jdd.gameserver.network.GameClient;

/**
 * @author Sdw
 */
public class RequestExJoinMpccRoom implements IClientIncomingPacket
{
	private int _roomId;
	
	@Override
	public boolean read(GameClient client, PacketReader packet)
	{
		_roomId = packet.readD();
		return true;
	}
	
	@Override
	public void run(GameClient client)
	{
		final PlayerInstance player = client.getPlayer();
		if ((player == null) || (player.getMatchingRoom() != null))
		{
			return;
		}
		
		final MatchingRoom room = MatchingRoomManager.getInstance().getCCMatchingRoom(_roomId);
		if (room != null)
		{
			room.addMember(player);
		}
	}
}
