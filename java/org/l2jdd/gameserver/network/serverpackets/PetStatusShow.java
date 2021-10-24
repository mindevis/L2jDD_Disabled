
package org.l2jdd.gameserver.network.serverpackets;

import org.l2jdd.commons.network.PacketWriter;
import org.l2jdd.gameserver.model.actor.Summon;
import org.l2jdd.gameserver.network.OutgoingPackets;

/**
 * @author Yme
 */
public class PetStatusShow implements IClientOutgoingPacket
{
	private final int _summonType;
	private final int _summonObjectId;
	
	public PetStatusShow(Summon summon)
	{
		_summonType = summon.getSummonType();
		_summonObjectId = summon.getObjectId();
	}
	
	@Override
	public boolean write(PacketWriter packet)
	{
		OutgoingPackets.PET_STATUS_SHOW.writeId(packet);
		
		packet.writeD(_summonType);
		packet.writeD(_summonObjectId);
		return true;
	}
}
