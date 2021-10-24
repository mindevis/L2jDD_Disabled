
package org.l2jdd.gameserver.network.serverpackets.ability;

import org.l2jdd.commons.network.PacketWriter;
import org.l2jdd.gameserver.network.OutgoingPackets;
import org.l2jdd.gameserver.network.serverpackets.IClientOutgoingPacket;

/**
 * @author UnAfraid
 */
public class ExCloseAPListWnd implements IClientOutgoingPacket
{
	public static final ExCloseAPListWnd STATIC_PACKET = new ExCloseAPListWnd();
	
	private ExCloseAPListWnd()
	{
	}
	
	@Override
	public boolean write(PacketWriter packet)
	{
		OutgoingPackets.EX_CLOSE_AP_LIST_WND.writeId(packet);
		
		return true;
	}
}