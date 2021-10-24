
package org.l2jdd.gameserver.network.serverpackets;

import org.l2jdd.commons.network.PacketWriter;
import org.l2jdd.gameserver.model.actor.Creature;
import org.l2jdd.gameserver.network.OutgoingPackets;

/**
 * @author devScarlet
 */
public class NicknameChanged implements IClientOutgoingPacket
{
	private final String _title;
	private final int _objectId;
	
	public NicknameChanged(Creature creature)
	{
		_objectId = creature.getObjectId();
		_title = creature.getTitle();
	}
	
	@Override
	public boolean write(PacketWriter packet)
	{
		OutgoingPackets.NICK_NAME_CHANGED.writeId(packet);
		
		packet.writeD(_objectId);
		packet.writeS(_title);
		return true;
	}
}
