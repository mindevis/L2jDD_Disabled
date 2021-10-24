
package org.l2jdd.gameserver.network.serverpackets;

import org.l2jdd.commons.network.PacketWriter;
import org.l2jdd.gameserver.model.Location;
import org.l2jdd.gameserver.network.OutgoingPackets;

public class ObservationReturn implements IClientOutgoingPacket
{
	private final Location _loc;
	
	public ObservationReturn(Location loc)
	{
		_loc = loc;
	}
	
	@Override
	public boolean write(PacketWriter packet)
	{
		OutgoingPackets.OBSERVER_END.writeId(packet);
		
		packet.writeD(_loc.getX());
		packet.writeD(_loc.getY());
		packet.writeD(_loc.getZ());
		return true;
	}
}
