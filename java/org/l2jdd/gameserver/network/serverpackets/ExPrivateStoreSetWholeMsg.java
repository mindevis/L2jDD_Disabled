
package org.l2jdd.gameserver.network.serverpackets;

import org.l2jdd.commons.network.PacketWriter;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.network.OutgoingPackets;

/**
 * @author KenM
 */
public class ExPrivateStoreSetWholeMsg implements IClientOutgoingPacket
{
	private final int _objectId;
	private final String _msg;
	
	public ExPrivateStoreSetWholeMsg(PlayerInstance player, String msg)
	{
		_objectId = player.getObjectId();
		_msg = msg;
	}
	
	public ExPrivateStoreSetWholeMsg(PlayerInstance player)
	{
		this(player, player.getSellList().getTitle());
	}
	
	@Override
	public boolean write(PacketWriter packet)
	{
		OutgoingPackets.EX_PRIVATE_STORE_WHOLE_MSG.writeId(packet);
		
		packet.writeD(_objectId);
		packet.writeS(_msg);
		return true;
	}
}
