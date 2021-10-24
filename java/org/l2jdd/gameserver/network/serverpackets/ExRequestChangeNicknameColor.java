
package org.l2jdd.gameserver.network.serverpackets;

import org.l2jdd.commons.network.PacketWriter;
import org.l2jdd.gameserver.network.OutgoingPackets;

/**
 * @author Gnacik
 */
public class ExRequestChangeNicknameColor implements IClientOutgoingPacket
{
	private final int _itemId;
	
	public ExRequestChangeNicknameColor(int itemId)
	{
		_itemId = itemId;
	}
	
	@Override
	public boolean write(PacketWriter packet)
	{
		OutgoingPackets.EX_CHANGE_NICKNAME_NCOLOR.writeId(packet);
		
		packet.writeD(_itemId);
		return true;
	}
}
