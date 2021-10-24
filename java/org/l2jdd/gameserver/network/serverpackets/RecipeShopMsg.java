
package org.l2jdd.gameserver.network.serverpackets;

import org.l2jdd.commons.network.PacketWriter;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.network.OutgoingPackets;

public class RecipeShopMsg implements IClientOutgoingPacket
{
	private final PlayerInstance _player;
	
	public RecipeShopMsg(PlayerInstance player)
	{
		_player = player;
	}
	
	@Override
	public boolean write(PacketWriter packet)
	{
		OutgoingPackets.RECIPE_SHOP_MSG.writeId(packet);
		
		packet.writeD(_player.getObjectId());
		packet.writeS(_player.getStoreName());
		return true;
	}
}
