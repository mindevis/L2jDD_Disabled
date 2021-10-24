
package org.l2jdd.gameserver.network.serverpackets;

import java.util.List;

import org.l2jdd.commons.network.PacketWriter;
import org.l2jdd.commons.util.Chronos;
import org.l2jdd.gameserver.instancemanager.MailManager;
import org.l2jdd.gameserver.model.Message;
import org.l2jdd.gameserver.network.OutgoingPackets;

/**
 * @author Migi, DS
 */
public class ExShowSentPostList implements IClientOutgoingPacket
{
	private final List<Message> _outbox;
	
	public ExShowSentPostList(int objectId)
	{
		_outbox = MailManager.getInstance().getOutbox(objectId);
	}
	
	@Override
	public boolean write(PacketWriter packet)
	{
		OutgoingPackets.EX_SHOW_SENT_POST_LIST.writeId(packet);
		
		packet.writeD((int) (Chronos.currentTimeMillis() / 1000));
		if ((_outbox != null) && !_outbox.isEmpty())
		{
			packet.writeD(_outbox.size());
			for (Message msg : _outbox)
			{
				packet.writeD(msg.getId());
				packet.writeS(msg.getSubject());
				packet.writeS(msg.getReceiverName());
				packet.writeD(msg.isLocked() ? 0x01 : 0x00);
				packet.writeD(msg.getExpirationSeconds());
				packet.writeD(msg.isUnread() ? 0x01 : 0x00);
				packet.writeD(0x01);
				packet.writeD(msg.hasAttachments() ? 0x01 : 0x00);
				packet.writeD(0x00);
			}
		}
		else
		{
			packet.writeD(0x00);
		}
		return true;
	}
}
