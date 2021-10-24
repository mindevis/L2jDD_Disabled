
package org.l2jdd.gameserver.network.serverpackets;

import org.l2jdd.commons.network.PacketWriter;
import org.l2jdd.gameserver.network.OutgoingPackets;

/**
 * @author nBd
 */
public class RequestEnchant implements IClientOutgoingPacket
{
	private final int _result;
	
	public RequestEnchant(int result)
	{
		_result = result;
	}
	
	@Override
	public boolean write(PacketWriter packet)
	{
		OutgoingPackets.EX_PRIVATE_STORE_WHOLE_MSG.writeId(packet);
		
		packet.writeD(_result);
		return true;
	}
}
