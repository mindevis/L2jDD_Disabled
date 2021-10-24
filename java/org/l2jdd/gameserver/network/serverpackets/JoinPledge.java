
package org.l2jdd.gameserver.network.serverpackets;

import org.l2jdd.commons.network.PacketWriter;
import org.l2jdd.gameserver.network.OutgoingPackets;

public class JoinPledge implements IClientOutgoingPacket
{
	private final int _pledgeId;
	
	public JoinPledge(int pledgeId)
	{
		_pledgeId = pledgeId;
	}
	
	@Override
	public boolean write(PacketWriter packet)
	{
		OutgoingPackets.JOIN_PLEDGE.writeId(packet);
		
		packet.writeD(_pledgeId);
		return true;
	}
}
