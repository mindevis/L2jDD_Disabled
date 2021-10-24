
package org.l2jdd.gameserver.network.serverpackets;

import org.l2jdd.commons.network.PacketWriter;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.network.OutgoingPackets;

/**
 * @author GodKratos
 */
public class ExBrPremiumState implements IClientOutgoingPacket
{
	private final PlayerInstance _player;
	
	public ExBrPremiumState(PlayerInstance player)
	{
		_player = player;
	}
	
	@Override
	public boolean write(PacketWriter packet)
	{
		OutgoingPackets.EX_BR_PREMIUM_STATE.writeId(packet);
		
		packet.writeD(_player.getObjectId());
		packet.writeC(_player.hasPremiumStatus() ? 0x01 : 0x00);
		return true;
	}
}
