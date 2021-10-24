
package org.l2jdd.gameserver.network.serverpackets;

import org.l2jdd.commons.network.PacketWriter;
import org.l2jdd.gameserver.enums.PartyDistributionType;
import org.l2jdd.gameserver.network.OutgoingPackets;

public class AskJoinParty implements IClientOutgoingPacket
{
	private final String _requestorName;
	private final PartyDistributionType _partyDistributionType;
	
	/**
	 * @param requestorName
	 * @param partyDistributionType
	 */
	public AskJoinParty(String requestorName, PartyDistributionType partyDistributionType)
	{
		_requestorName = requestorName;
		_partyDistributionType = partyDistributionType;
	}
	
	@Override
	public boolean write(PacketWriter packet)
	{
		OutgoingPackets.ASK_JOIN_PARTY.writeId(packet);
		
		packet.writeS(_requestorName);
		packet.writeD(_partyDistributionType.getId());
		return true;
	}
}
