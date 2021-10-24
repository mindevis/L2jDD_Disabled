
package org.l2jdd.gameserver.network.serverpackets;

import org.l2jdd.commons.network.PacketWriter;
import org.l2jdd.gameserver.network.OutgoingPackets;

public class ShowMiniMap implements IClientOutgoingPacket
{
	private final int _mapId;
	
	public ShowMiniMap(int mapId)
	{
		_mapId = mapId;
	}
	
	@Override
	public boolean write(PacketWriter packet)
	{
		OutgoingPackets.SHOW_MINIMAP.writeId(packet);
		
		packet.writeD(_mapId);
		packet.writeC(0x00); // Seven Signs state
		return true;
	}
}
