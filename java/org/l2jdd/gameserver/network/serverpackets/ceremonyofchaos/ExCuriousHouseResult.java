
package org.l2jdd.gameserver.network.serverpackets.ceremonyofchaos;

import org.l2jdd.commons.network.PacketWriter;
import org.l2jdd.gameserver.enums.CeremonyOfChaosResult;
import org.l2jdd.gameserver.instancemanager.CeremonyOfChaosManager;
import org.l2jdd.gameserver.model.ceremonyofchaos.CeremonyOfChaosEvent;
import org.l2jdd.gameserver.network.OutgoingPackets;
import org.l2jdd.gameserver.network.serverpackets.IClientOutgoingPacket;

/**
 * @author Sdw
 */
public class ExCuriousHouseResult implements IClientOutgoingPacket
{
	private final CeremonyOfChaosResult _result;
	private final CeremonyOfChaosEvent _event;
	
	public ExCuriousHouseResult(CeremonyOfChaosResult result, CeremonyOfChaosEvent event)
	{
		_result = result;
		_event = event;
	}
	
	@Override
	public boolean write(PacketWriter packet)
	{
		OutgoingPackets.EX_CURIOUS_HOUSE_RESULT.writeId(packet);
		packet.writeD(_event.getId());
		packet.writeH(_result.ordinal());
		packet.writeD(CeremonyOfChaosManager.getInstance().getMaxPlayersInArena());
		packet.writeD(_event.getMembers().size());
		_event.getMembers().values().forEach(m ->
		{
			packet.writeD(m.getObjectId());
			packet.writeD(m.getPosition());
			packet.writeD(m.getClassId());
			packet.writeD(m.getLifeTime());
			packet.writeD(m.getScore());
		});
		return true;
	}
}
