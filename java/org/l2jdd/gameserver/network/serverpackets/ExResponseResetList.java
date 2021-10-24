
package org.l2jdd.gameserver.network.serverpackets;

import org.l2jdd.commons.network.PacketWriter;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.network.OutgoingPackets;

/**
 * @author Sdw
 */
public class ExResponseResetList implements IClientOutgoingPacket
{
	private final PlayerInstance _player;
	
	public ExResponseResetList(PlayerInstance player)
	{
		_player = player;
	}
	
	@Override
	public boolean write(PacketWriter packet)
	{
		OutgoingPackets.EX_RESPONSE_RESET_LIST.writeId(packet);
		
		packet.writeQ(_player.getAdena());
		packet.writeQ(_player.getBeautyTickets());
		
		packet.writeD(_player.getAppearance().getHairStyle());
		packet.writeD(_player.getAppearance().getHairColor());
		packet.writeD(_player.getAppearance().getFace());
		return true;
	}
}
