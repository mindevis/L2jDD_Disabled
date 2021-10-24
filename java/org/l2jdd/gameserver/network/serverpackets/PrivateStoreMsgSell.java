
package org.l2jdd.gameserver.network.serverpackets;

import org.l2jdd.commons.network.PacketWriter;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.network.OutgoingPackets;

public class PrivateStoreMsgSell implements IClientOutgoingPacket
{
	private final int _objId;
	private String _storeMsg;
	
	public PrivateStoreMsgSell(PlayerInstance player)
	{
		_objId = player.getObjectId();
		if ((player.getSellList() != null) || player.isSellingBuffs())
		{
			_storeMsg = player.getSellList().getTitle();
		}
	}
	
	@Override
	public boolean write(PacketWriter packet)
	{
		OutgoingPackets.PRIVATE_STORE_MSG.writeId(packet);
		
		packet.writeD(_objId);
		packet.writeS(_storeMsg);
		return true;
	}
}
