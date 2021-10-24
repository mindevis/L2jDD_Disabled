
package org.l2jdd.gameserver.network.serverpackets;

import org.l2jdd.commons.network.PacketWriter;
import org.l2jdd.gameserver.model.WorldObject;
import org.l2jdd.gameserver.network.OutgoingPackets;

public class DeleteObject implements IClientOutgoingPacket
{
	private final int _objectId;
	
	public DeleteObject(WorldObject obj)
	{
		_objectId = obj.getObjectId();
	}
	
	public DeleteObject(int objectId)
	{
		_objectId = objectId;
	}
	
	@Override
	public boolean write(PacketWriter packet)
	{
		OutgoingPackets.DELETE_OBJECT.writeId(packet);
		
		packet.writeD(_objectId);
		packet.writeC(0x00); // c2
		return true;
	}
}
