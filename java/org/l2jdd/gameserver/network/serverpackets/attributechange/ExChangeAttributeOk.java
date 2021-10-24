
package org.l2jdd.gameserver.network.serverpackets.attributechange;

import org.l2jdd.commons.network.PacketWriter;
import org.l2jdd.gameserver.network.OutgoingPackets;
import org.l2jdd.gameserver.network.serverpackets.IClientOutgoingPacket;

/**
 * @author Mobius
 */
public class ExChangeAttributeOk implements IClientOutgoingPacket
{
	public static final IClientOutgoingPacket STATIC = new ExChangeAttributeOk();
	
	@Override
	public boolean write(PacketWriter packet)
	{
		OutgoingPackets.EX_CHANGE_ATTRIBUTE_OK.writeId(packet);
		return true;
	}
}