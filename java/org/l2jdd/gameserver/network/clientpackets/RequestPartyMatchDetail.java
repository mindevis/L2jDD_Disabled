
package org.l2jdd.gameserver.network.clientpackets;

import org.l2jdd.commons.network.PacketReader;
import org.l2jdd.gameserver.instancemanager.MatchingRoomManager;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.model.matching.MatchingRoom;
import org.l2jdd.gameserver.network.GameClient;

/**
 * @author Gnacik
 */
public class RequestPartyMatchDetail implements IClientIncomingPacket
{
	private int _roomId;
	private int _location;
	private int _level;
	
	@Override
	public boolean read(GameClient client, PacketReader packet)
	{
		_roomId = packet.readD();
		_location = packet.readD();
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
		
		if (player.isInMatchingRoom())
		{
			return;
		}
		
		final MatchingRoom room = _roomId > 0 ? MatchingRoomManager.getInstance().getPartyMathchingRoom(_roomId) : MatchingRoomManager.getInstance().getPartyMathchingRoom(_location, _level);
		if (room != null)
		{
			room.addMember(player);
		}
	}
}
