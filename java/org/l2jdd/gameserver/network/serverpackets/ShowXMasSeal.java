
package org.l2jdd.gameserver.network.serverpackets;

import org.l2jdd.commons.network.PacketWriter;
import org.l2jdd.gameserver.network.OutgoingPackets;

/**
 * @author devScarlet, mrTJO
 */
public class ShowXMasSeal implements IClientOutgoingPacket
{
	private final int _item;
	
	public ShowXMasSeal(int item)
	{
		_item = item;
	}
	
	@Override
	public boolean write(PacketWriter packet)
	{
		OutgoingPackets.SHOW_XMAS_SEAL.writeId(packet);
		
		packet.writeD(_item);
		return true;
	}
}
