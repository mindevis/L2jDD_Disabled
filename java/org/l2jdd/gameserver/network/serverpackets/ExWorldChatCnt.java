
package org.l2jdd.gameserver.network.serverpackets;

import org.l2jdd.Config;
import org.l2jdd.commons.network.PacketWriter;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.network.OutgoingPackets;

/**
 * @author UnAfraid
 */
public class ExWorldChatCnt implements IClientOutgoingPacket
{
	private final int _points;
	
	public ExWorldChatCnt(PlayerInstance player)
	{
		_points = player.getLevel() < Config.WORLD_CHAT_MIN_LEVEL ? 0 : Math.max(player.getWorldChatPoints() - player.getWorldChatUsed(), 0);
	}
	
	@Override
	public boolean write(PacketWriter packet)
	{
		OutgoingPackets.EX_WORLD_CHAT_CNT.writeId(packet);
		
		packet.writeD(_points);
		return true;
	}
}
