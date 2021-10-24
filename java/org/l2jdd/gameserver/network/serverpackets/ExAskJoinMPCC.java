
package org.l2jdd.gameserver.network.serverpackets;

import org.l2jdd.commons.network.PacketWriter;
import org.l2jdd.gameserver.network.OutgoingPackets;

/**
 * Asks the player to join a CC
 * @author chris_00
 */
public class ExAskJoinMPCC implements IClientOutgoingPacket
{
	private final String _requestorName;
	
	/**
	 * @param requestorName
	 */
	public ExAskJoinMPCC(String requestorName)
	{
		_requestorName = requestorName;
	}
	
	@Override
	public boolean write(PacketWriter packet)
	{
		OutgoingPackets.EX_ASK_JOIN_MPCC.writeId(packet);
		
		packet.writeS(_requestorName); // name of CCLeader
		packet.writeD(0x00); // TODO: Find me
		return true;
	}
}
