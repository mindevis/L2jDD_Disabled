
package org.l2jdd.gameserver.network.serverpackets;

import org.l2jdd.commons.network.PacketWriter;
import org.l2jdd.gameserver.network.OutgoingPackets;

/**
 * Show Confirm Dialog for 10 seconds
 * @author mrTJO
 */
public class ExCubeGameRequestReady implements IClientOutgoingPacket
{
	public static final ExCubeGameRequestReady STATIC_PACKET = new ExCubeGameRequestReady();
	
	private ExCubeGameRequestReady()
	{
	}
	
	@Override
	public boolean write(PacketWriter packet)
	{
		OutgoingPackets.EX_BLOCK_UP_SET_LIST.writeId(packet);
		
		packet.writeD(0x04);
		return true;
	}
}
