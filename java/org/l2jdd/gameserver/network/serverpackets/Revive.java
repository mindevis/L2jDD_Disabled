
package org.l2jdd.gameserver.network.serverpackets;

import org.l2jdd.commons.network.PacketWriter;
import org.l2jdd.gameserver.model.WorldObject;
import org.l2jdd.gameserver.network.OutgoingPackets;

public class Revive implements IClientOutgoingPacket
{
	private final int _objectId;
	
	public Revive(WorldObject obj)
	{
		_objectId = obj.getObjectId();
	}
	
	@Override
	public boolean write(PacketWriter packet)
	{
		OutgoingPackets.REVIVE.writeId(packet);
		
		packet.writeD(_objectId);
		return true;
	}
}
