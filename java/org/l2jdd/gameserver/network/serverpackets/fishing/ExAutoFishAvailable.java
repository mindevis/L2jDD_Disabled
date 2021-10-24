
package org.l2jdd.gameserver.network.serverpackets.fishing;

import org.l2jdd.commons.network.PacketWriter;
import org.l2jdd.gameserver.network.OutgoingPackets;
import org.l2jdd.gameserver.network.serverpackets.IClientOutgoingPacket;

/**
 * @author bit
 */
public class ExAutoFishAvailable implements IClientOutgoingPacket
{
	public static final ExAutoFishAvailable YES = new ExAutoFishAvailable(true);
	public static final ExAutoFishAvailable NO = new ExAutoFishAvailable(false);
	
	private final boolean _available;
	
	private ExAutoFishAvailable(boolean available)
	{
		_available = available;
	}
	
	@Override
	public boolean write(PacketWriter packet)
	{
		OutgoingPackets.EX_AUTO_FISH_AVAILABLE.writeId(packet);
		packet.writeC(_available ? 1 : 0);
		return true;
	}
}
