
package org.l2jdd.gameserver.network.serverpackets;

import org.l2jdd.commons.network.PacketWriter;
import org.l2jdd.gameserver.network.OutgoingPackets;

public class StopRotation implements IClientOutgoingPacket
{
	private final int _objectId;
	private final int _degree;
	private final int _speed;
	
	public StopRotation(int objectId, int degree, int speed)
	{
		_objectId = objectId;
		_degree = degree;
		_speed = speed;
	}
	
	@Override
	public boolean write(PacketWriter packet)
	{
		OutgoingPackets.FINISH_ROTATING.writeId(packet);
		
		packet.writeD(_objectId);
		packet.writeD(_degree);
		packet.writeD(_speed);
		packet.writeD(0); // ?
		return true;
	}
}
