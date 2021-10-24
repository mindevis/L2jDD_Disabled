
package org.l2jdd.gameserver.network.serverpackets;

import org.l2jdd.commons.network.PacketWriter;
import org.l2jdd.gameserver.model.Party;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.network.OutgoingPackets;

/**
 * @author chris_00
 */
public class ExMPCCShowPartyMemberInfo implements IClientOutgoingPacket
{
	private final Party _party;
	
	public ExMPCCShowPartyMemberInfo(Party party)
	{
		_party = party;
	}
	
	@Override
	public boolean write(PacketWriter packet)
	{
		OutgoingPackets.EX_MPCCSHOW_PARTY_MEMBER_INFO.writeId(packet);
		
		packet.writeD(_party.getMemberCount());
		for (PlayerInstance pc : _party.getMembers())
		{
			packet.writeS(pc.getName());
			packet.writeD(pc.getObjectId());
			packet.writeD(pc.getClassId().getId());
		}
		return true;
	}
}
