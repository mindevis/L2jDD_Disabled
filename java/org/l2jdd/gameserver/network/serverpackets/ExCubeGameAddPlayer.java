
package org.l2jdd.gameserver.network.serverpackets;

import org.l2jdd.commons.network.PacketWriter;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.network.OutgoingPackets;

/**
 * @author mrTJO
 */
public class ExCubeGameAddPlayer implements IClientOutgoingPacket
{
	PlayerInstance _player;
	boolean _isRedTeam;
	
	/**
	 * Add Player To Minigame Waiting List
	 * @param player Player Instance
	 * @param isRedTeam Is Player from Red Team?
	 */
	public ExCubeGameAddPlayer(PlayerInstance player, boolean isRedTeam)
	{
		_player = player;
		_isRedTeam = isRedTeam;
	}
	
	@Override
	public boolean write(PacketWriter packet)
	{
		OutgoingPackets.EX_BLOCK_UP_SET_LIST.writeId(packet);
		
		packet.writeD(0x01);
		
		packet.writeD(0xffffffff);
		
		packet.writeD(_isRedTeam ? 0x01 : 0x00);
		packet.writeD(_player.getObjectId());
		packet.writeS(_player.getName());
		return true;
	}
}
