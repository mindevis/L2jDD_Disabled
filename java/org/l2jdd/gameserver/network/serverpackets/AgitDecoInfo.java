
package org.l2jdd.gameserver.network.serverpackets;

import org.l2jdd.commons.network.PacketWriter;
import org.l2jdd.gameserver.model.residences.AbstractResidence;
import org.l2jdd.gameserver.model.residences.ResidenceFunctionType;
import org.l2jdd.gameserver.network.OutgoingPackets;

/**
 * @author Steuf, UnAfraid
 */
public class AgitDecoInfo implements IClientOutgoingPacket
{
	private final AbstractResidence _residense;
	
	public AgitDecoInfo(AbstractResidence residense)
	{
		_residense = residense;
	}
	
	@Override
	public boolean write(PacketWriter packet)
	{
		OutgoingPackets.AGIT_DECO_INFO.writeId(packet);
		packet.writeD(_residense.getResidenceId());
		for (ResidenceFunctionType type : ResidenceFunctionType.values())
		{
			if (type == ResidenceFunctionType.NONE)
			{
				continue;
			}
			packet.writeC(_residense.hasFunction(type) ? 0x01 : 0x00);
		}
		
		// Unknown
		packet.writeD(0); // TODO: Find me!
		packet.writeD(0); // TODO: Find me!
		packet.writeD(0); // TODO: Find me!
		packet.writeD(0); // TODO: Find me!
		packet.writeD(0); // TODO: Find me!
		return true;
	}
}
