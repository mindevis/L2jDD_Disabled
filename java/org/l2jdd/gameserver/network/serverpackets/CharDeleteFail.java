
package org.l2jdd.gameserver.network.serverpackets;

import org.l2jdd.commons.network.PacketWriter;
import org.l2jdd.gameserver.enums.CharacterDeleteFailType;
import org.l2jdd.gameserver.network.OutgoingPackets;

public class CharDeleteFail implements IClientOutgoingPacket
{
	private final int _error;
	
	public CharDeleteFail(CharacterDeleteFailType type)
	{
		_error = type.ordinal();
	}
	
	@Override
	public boolean write(PacketWriter packet)
	{
		OutgoingPackets.CHARACTER_DELETE_FAIL.writeId(packet);
		
		packet.writeD(_error);
		return true;
	}
}
