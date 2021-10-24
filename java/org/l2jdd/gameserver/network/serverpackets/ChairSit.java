
package org.l2jdd.gameserver.network.serverpackets;

import org.l2jdd.commons.network.PacketWriter;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.network.OutgoingPackets;

public class ChairSit implements IClientOutgoingPacket
{
	private final PlayerInstance _player;
	private final int _staticObjectId;
	
	/**
	 * @param player
	 * @param staticObjectId
	 */
	public ChairSit(PlayerInstance player, int staticObjectId)
	{
		_player = player;
		_staticObjectId = staticObjectId;
	}
	
	@Override
	public boolean write(PacketWriter packet)
	{
		OutgoingPackets.CHAIR_SIT.writeId(packet);
		
		packet.writeD(_player.getObjectId());
		packet.writeD(_staticObjectId);
		return true;
	}
}
