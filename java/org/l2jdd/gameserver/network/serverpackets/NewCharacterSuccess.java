
package org.l2jdd.gameserver.network.serverpackets;

import java.util.ArrayList;
import java.util.List;

import org.l2jdd.commons.network.PacketWriter;
import org.l2jdd.gameserver.model.actor.templates.PlayerTemplate;
import org.l2jdd.gameserver.network.OutgoingPackets;

public class NewCharacterSuccess implements IClientOutgoingPacket
{
	private final List<PlayerTemplate> _chars = new ArrayList<>();
	
	public void addChar(PlayerTemplate template)
	{
		_chars.add(template);
	}
	
	@Override
	public boolean write(PacketWriter packet)
	{
		OutgoingPackets.NEW_CHARACTER_SUCCESS.writeId(packet);
		
		packet.writeD(_chars.size());
		for (PlayerTemplate chr : _chars)
		{
			if (chr == null)
			{
				continue;
			}
			
			// TODO: Unhardcode these
			packet.writeD(chr.getRace().ordinal());
			packet.writeD(chr.getClassId().getId());
			
			packet.writeD(99);
			packet.writeD(chr.getBaseSTR());
			packet.writeD(1);
			
			packet.writeD(99);
			packet.writeD(chr.getBaseDEX());
			packet.writeD(1);
			
			packet.writeD(99);
			packet.writeD(chr.getBaseCON());
			packet.writeD(1);
			
			packet.writeD(99);
			packet.writeD(chr.getBaseINT());
			packet.writeD(1);
			
			packet.writeD(99);
			packet.writeD(chr.getBaseWIT());
			packet.writeD(1);
			
			packet.writeD(99);
			packet.writeD(chr.getBaseMEN());
			packet.writeD(1);
		}
		return true;
	}
}
