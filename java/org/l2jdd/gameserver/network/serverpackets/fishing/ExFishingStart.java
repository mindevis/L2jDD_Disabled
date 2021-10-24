
package org.l2jdd.gameserver.network.serverpackets.fishing;

import org.l2jdd.commons.network.PacketWriter;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.model.interfaces.ILocational;
import org.l2jdd.gameserver.network.OutgoingPackets;
import org.l2jdd.gameserver.network.serverpackets.IClientOutgoingPacket;

/**
 * @author -Wooden-
 */
public class ExFishingStart implements IClientOutgoingPacket
{
	private final PlayerInstance _player;
	private final int _fishType;
	private final ILocational _baitLocation;
	
	/**
	 * @param player
	 * @param fishType
	 * @param baitLocation
	 */
	public ExFishingStart(PlayerInstance player, int fishType, ILocational baitLocation)
	{
		_player = player;
		_fishType = fishType;
		_baitLocation = baitLocation;
	}
	
	@Override
	public boolean write(PacketWriter packet)
	{
		OutgoingPackets.EX_FISHING_START.writeId(packet);
		packet.writeD(_player.getObjectId());
		packet.writeC(_fishType);
		packet.writeD(_baitLocation.getX());
		packet.writeD(_baitLocation.getY());
		packet.writeD(_baitLocation.getZ());
		packet.writeC(0x01); // 0 = newbie, 1 = normal, 2 = night
		return true;
	}
}
