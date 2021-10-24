
package org.l2jdd.gameserver.network.serverpackets;

import java.time.LocalDateTime;
import java.time.ZoneId;

import org.l2jdd.commons.network.PacketWriter;
import org.l2jdd.gameserver.network.OutgoingPackets;

public class PvpBookList implements IClientOutgoingPacket
{
	public PvpBookList()
	{
	}
	
	@Override
	public boolean write(PacketWriter packet)
	{
		OutgoingPackets.EX_PVPBOOK_LIST.writeId(packet);
		
		final int size = 1;
		packet.writeD(4); // show killer's location count
		packet.writeD(5); // teleport count
		packet.writeD(size); // killer count
		for (int i = 0; i < size; i++)
		{
			packet.writeString("killer" + i); // killer name
			packet.writeString("clanKiller" + i); // killer clan name
			packet.writeD(15); // killer level
			packet.writeD(2); // killer race
			packet.writeD(10); // killer class
			packet.writeD((int) LocalDateTime.now().atZone(ZoneId.systemDefault()).toEpochSecond()); // kill time
			packet.writeC(1); // is online
		}
		return true;
	}
}
