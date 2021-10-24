
package org.l2jdd.gameserver.network.serverpackets;

import org.l2jdd.commons.network.PacketWriter;
import org.l2jdd.gameserver.network.OutgoingPackets;

/**
 * @author JIV
 */
public class ExBaseAttributeCancelResult implements IClientOutgoingPacket
{
	private final int _objId;
	private final byte _attribute;
	
	public ExBaseAttributeCancelResult(int objId, byte attribute)
	{
		_objId = objId;
		_attribute = attribute;
	}
	
	@Override
	public boolean write(PacketWriter packet)
	{
		OutgoingPackets.EX_BASE_ATTRIBUTE_CANCEL_RESULT.writeId(packet);
		
		packet.writeD(0x01); // result
		packet.writeD(_objId);
		packet.writeD(_attribute);
		return true;
	}
}
