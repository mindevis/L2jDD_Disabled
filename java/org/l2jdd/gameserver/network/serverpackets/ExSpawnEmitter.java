
package org.l2jdd.gameserver.network.serverpackets;

import org.l2jdd.commons.network.PacketWriter;
import org.l2jdd.gameserver.model.actor.Npc;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.network.OutgoingPackets;

/**
 * @author KenM
 */
public class ExSpawnEmitter implements IClientOutgoingPacket
{
	private final int _playerObjectId;
	private final int _npcObjectId;
	
	public ExSpawnEmitter(int playerObjectId, int npcObjectId)
	{
		_playerObjectId = playerObjectId;
		_npcObjectId = npcObjectId;
	}
	
	public ExSpawnEmitter(PlayerInstance player, Npc npc)
	{
		this(player.getObjectId(), npc.getObjectId());
	}
	
	@Override
	public boolean write(PacketWriter packet)
	{
		OutgoingPackets.EX_SPAWN_EMITTER.writeId(packet);
		
		packet.writeD(_npcObjectId);
		packet.writeD(_playerObjectId);
		packet.writeD(0x00); // ?
		return true;
	}
}
