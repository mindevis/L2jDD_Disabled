
package org.l2jdd.gameserver.network.serverpackets;

import org.l2jdd.commons.network.PacketWriter;
import org.l2jdd.gameserver.network.OutgoingPackets;

public class StartRotation implements IClientOutgoingPacket
{
	private final int _objectId;
	private final int _degree;
	private final int _side;
	private final int _speed;
	
	public StartRotation(int objectId, int degree, int side, int speed)
	{
		_objectId = objectId;
		_degree = degree;
		_side = side;
		_speed = speed;
	}
	
	@Override
	public boolean write(PacketWriter packet)
	{
		OutgoingPackets.START_ROTATING.writeId(packet);
		
		packet.writeD(_objectId);
		packet.writeD(_degree);
		packet.writeD(_side);
		packet.writeD(_speed);
		return true;
	}
}
