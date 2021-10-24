
package org.l2jdd.gameserver.network.serverpackets;

import org.l2jdd.commons.network.PacketWriter;
import org.l2jdd.gameserver.enums.PartyDistributionType;
import org.l2jdd.gameserver.network.OutgoingPackets;

/**
 * @author JIV
 */
public class ExSetPartyLooting implements IClientOutgoingPacket
{
	private final int _result;
	private final PartyDistributionType _partyDistributionType;
	
	public ExSetPartyLooting(int result, PartyDistributionType partyDistributionType)
	{
		_result = result;
		_partyDistributionType = partyDistributionType;
	}
	
	@Override
	public boolean write(PacketWriter packet)
	{
		OutgoingPackets.EX_SET_PARTY_LOOTING.writeId(packet);
		
		packet.writeD(_result);
		packet.writeD(_partyDistributionType.getId());
		return true;
	}
}
