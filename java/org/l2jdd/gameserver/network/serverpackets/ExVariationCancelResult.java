
package org.l2jdd.gameserver.network.serverpackets;

import org.l2jdd.commons.network.PacketWriter;
import org.l2jdd.gameserver.network.OutgoingPackets;

public class ExVariationCancelResult implements IClientOutgoingPacket
{
	public static final ExVariationCancelResult STATIC_PACKET_SUCCESS = new ExVariationCancelResult(1);
	public static final ExVariationCancelResult STATIC_PACKET_FAILURE = new ExVariationCancelResult(0);
	
	private final int _result;
	
	private ExVariationCancelResult(int result)
	{
		_result = result;
	}
	
	@Override
	public boolean write(PacketWriter packet)
	{
		OutgoingPackets.EX_VARIATION_CANCEL_RESULT.writeId(packet);
		
		packet.writeD(_result);
		return true;
	}
}
