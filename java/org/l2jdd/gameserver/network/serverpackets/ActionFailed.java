
package org.l2jdd.gameserver.network.serverpackets;

import java.util.EnumMap;
import java.util.Map;

import org.l2jdd.commons.network.PacketWriter;
import org.l2jdd.gameserver.model.skills.SkillCastingType;
import org.l2jdd.gameserver.network.OutgoingPackets;

public class ActionFailed implements IClientOutgoingPacket
{
	public static final ActionFailed STATIC_PACKET = new ActionFailed();
	private static final Map<SkillCastingType, ActionFailed> STATIC_PACKET_BY_CASTING_TYPE = new EnumMap<>(SkillCastingType.class);
	
	static
	{
		for (SkillCastingType castingType : SkillCastingType.values())
		{
			STATIC_PACKET_BY_CASTING_TYPE.put(castingType, new ActionFailed(castingType.getClientBarId()));
		}
	}
	
	private final int _castingType;
	
	private ActionFailed()
	{
		_castingType = 0;
	}
	
	private ActionFailed(int castingType)
	{
		_castingType = castingType;
	}
	
	public static ActionFailed get(SkillCastingType castingType)
	{
		return STATIC_PACKET_BY_CASTING_TYPE.getOrDefault(castingType, STATIC_PACKET);
	}
	
	@Override
	public boolean write(PacketWriter packet)
	{
		OutgoingPackets.ACTION_FAIL.writeId(packet);
		
		packet.writeD(_castingType); // MagicSkillUse castingType
		return true;
	}
}
