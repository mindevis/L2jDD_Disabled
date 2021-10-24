
package org.l2jdd.gameserver.network.serverpackets;

import org.l2jdd.commons.network.PacketWriter;
import org.l2jdd.gameserver.network.OutgoingPackets;

/**
 * @author Kerberos
 */
public class GMHide implements IClientOutgoingPacket
{
	private final int _mode;
	
	/**
	 * @param mode (0 = display windows, 1 = hide windows)
	 */
	public GMHide(int mode)
	{
		_mode = mode;
	}
	
	@Override
	public boolean write(PacketWriter packet)
	{
		OutgoingPackets.GM_HIDE.writeId(packet);
		
		packet.writeD(_mode);
		return true;
	}
}
