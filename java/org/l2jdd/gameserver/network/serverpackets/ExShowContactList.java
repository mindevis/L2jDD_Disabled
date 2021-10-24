
package org.l2jdd.gameserver.network.serverpackets;

import java.util.Set;

import org.l2jdd.commons.network.PacketWriter;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.network.OutgoingPackets;

/**
 * @author UnAfraid, mrTJO
 */
public class ExShowContactList implements IClientOutgoingPacket
{
	private final Set<String> _contacts;
	
	public ExShowContactList(PlayerInstance player)
	{
		_contacts = player.getContactList().getAllContacts();
	}
	
	@Override
	public boolean write(PacketWriter packet)
	{
		OutgoingPackets.EX_RECEIVE_SHOW_POST_FRIEND.writeId(packet);
		
		packet.writeD(_contacts.size());
		_contacts.forEach(packet::writeS);
		return true;
	}
}
