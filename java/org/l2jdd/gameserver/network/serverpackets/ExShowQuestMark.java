
package org.l2jdd.gameserver.network.serverpackets;

import org.l2jdd.commons.network.PacketWriter;
import org.l2jdd.gameserver.network.OutgoingPackets;

/**
 * @author Luca Baldi
 */
public class ExShowQuestMark implements IClientOutgoingPacket
{
	private final int _questId;
	private final int _questState;
	
	public ExShowQuestMark(int questId, int questState)
	{
		_questId = questId;
		_questState = questState;
	}
	
	@Override
	public boolean write(PacketWriter packet)
	{
		OutgoingPackets.EX_SHOW_QUEST_MARK.writeId(packet);
		
		packet.writeD(_questId);
		packet.writeD(_questState);
		return true;
	}
}
