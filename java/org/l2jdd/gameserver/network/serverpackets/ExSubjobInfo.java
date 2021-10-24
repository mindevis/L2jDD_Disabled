
package org.l2jdd.gameserver.network.serverpackets;

import java.util.ArrayList;
import java.util.List;

import org.l2jdd.commons.network.PacketWriter;
import org.l2jdd.gameserver.enums.SubclassInfoType;
import org.l2jdd.gameserver.enums.SubclassType;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.model.holders.SubClassHolder;
import org.l2jdd.gameserver.network.OutgoingPackets;

/**
 * @author Sdw
 */
public class ExSubjobInfo implements IClientOutgoingPacket
{
	private final int _currClassId;
	private final int _currRace;
	private final int _type;
	private final List<SubInfo> _subs;
	
	public ExSubjobInfo(PlayerInstance player, SubclassInfoType type)
	{
		_currClassId = player.getClassId().getId();
		_currRace = player.getRace().ordinal();
		_type = type.ordinal();
		_subs = new ArrayList<>();
		_subs.add(0, new SubInfo(player));
		for (SubClassHolder sub : player.getSubClasses().values())
		{
			_subs.add(new SubInfo(sub));
		}
	}
	
	private final class SubInfo
	{
		private final int _index;
		private final int _classId;
		private final int _level;
		private final int _type;
		
		public SubInfo(SubClassHolder sub)
		{
			_index = sub.getClassIndex();
			_classId = sub.getClassId();
			_level = sub.getLevel();
			_type = sub.isDualClass() ? SubclassType.DUALCLASS.ordinal() : SubclassType.SUBCLASS.ordinal();
		}
		
		public SubInfo(PlayerInstance player)
		{
			_index = 0;
			_classId = player.getBaseClass();
			_level = player.getStat().getBaseLevel();
			_type = SubclassType.BASECLASS.ordinal();
		}
		
		public int getIndex()
		{
			return _index;
		}
		
		public int getClassId()
		{
			return _classId;
		}
		
		public int getLevel()
		{
			return _level;
		}
		
		public int getType()
		{
			return _type;
		}
	}
	
	@Override
	public boolean write(PacketWriter packet)
	{
		OutgoingPackets.EX_SUBJOB_INFO.writeId(packet);
		
		packet.writeC(_type);
		packet.writeD(_currClassId);
		packet.writeD(_currRace);
		packet.writeD(_subs.size());
		for (SubInfo sub : _subs)
		{
			packet.writeD(sub.getIndex());
			packet.writeD(sub.getClassId());
			packet.writeD(sub.getLevel());
			packet.writeC(sub.getType());
		}
		return true;
	}
}