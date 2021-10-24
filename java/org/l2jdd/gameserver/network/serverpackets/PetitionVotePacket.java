
package org.l2jdd.gameserver.network.serverpackets;

import org.l2jdd.commons.network.PacketWriter;
import org.l2jdd.gameserver.network.OutgoingPackets;

/**
 * @author Plim
 */
public class PetitionVotePacket implements IClientOutgoingPacket
{
	public static final PetitionVotePacket STATIC_PACKET = new PetitionVotePacket();
	
	private PetitionVotePacket()
	{
	}
	
	@Override
	public boolean write(PacketWriter packet)
	{
		OutgoingPackets.PETITION_VOTE.writeId(packet);
		return true;
	}
}
