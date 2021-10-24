
package org.l2jdd.gameserver.network.serverpackets;

import org.l2jdd.commons.network.PacketWriter;
import org.l2jdd.gameserver.network.OutgoingPackets;

/**
 * @author Mobius
 */
public class TutorialShowQuestionMark implements IClientOutgoingPacket
{
	private final int _markId;
	private final int _markType;
	
	public TutorialShowQuestionMark(int markId, int markType)
	{
		_markId = markId;
		_markType = markType;
	}
	
	@Override
	public boolean write(PacketWriter packet)
	{
		OutgoingPackets.TUTORIAL_SHOW_QUESTION_MARK.writeId(packet);
		
		packet.writeC(_markType);
		packet.writeD(_markId);
		return true;
	}
}