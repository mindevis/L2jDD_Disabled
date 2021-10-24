
package org.l2jdd.gameserver.network.serverpackets;

import org.l2jdd.commons.network.PacketWriter;
import org.l2jdd.gameserver.network.OutgoingPackets;

/**
 * @author Sdw
 */
public class ExChangeToAwakenedClass implements IClientOutgoingPacket
{
	private final int _classId;
	
	public ExChangeToAwakenedClass(int classId)
	{
		_classId = classId;
	}
	
	@Override
	public boolean write(PacketWriter packet)
	{
		OutgoingPackets.EX_CHANGE_TO_AWAKENED_CLASS.writeId(packet);
		
		packet.writeD(_classId);
		return true;
	}
}