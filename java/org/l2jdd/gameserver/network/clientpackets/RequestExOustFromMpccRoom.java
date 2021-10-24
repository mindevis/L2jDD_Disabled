
package org.l2jdd.gameserver.network.clientpackets;

import org.l2jdd.commons.network.PacketReader;
import org.l2jdd.gameserver.enums.MatchingRoomType;
import org.l2jdd.gameserver.model.World;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.model.matching.MatchingRoom;
import org.l2jdd.gameserver.network.GameClient;

/**
 * @author jeremy
 */
public class RequestExOustFromMpccRoom implements IClientIncomingPacket
{
	private int _objectId;
	
	@Override
	public boolean read(GameClient client, PacketReader packet)
	{
		_objectId = packet.readD();
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
		
		final MatchingRoom room = player.getMatchingRoom();
		if ((room != null) && (room.getLeader() == player) && (room.getRoomType() == MatchingRoomType.COMMAND_CHANNEL))
		{
			final PlayerInstance target = World.getInstance().getPlayer(_objectId);
			if (target != null)
			{
				room.deleteMember(target, true);
			}
		}
	}
}
