
package org.l2jdd.gameserver.network.serverpackets;

import org.l2jdd.commons.network.PacketWriter;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.model.zone.ZoneId;
import org.l2jdd.gameserver.network.OutgoingPackets;

/**
 * @author Luca Baldi
 */
public class EtcStatusUpdate implements IClientOutgoingPacket
{
	private final PlayerInstance _player;
	private int _mask;
	
	public EtcStatusUpdate(PlayerInstance player)
	{
		_player = player;
		_mask = _player.getMessageRefusal() || _player.isChatBanned() || _player.isSilenceMode() ? 1 : 0;
		_mask |= _player.isInsideZone(ZoneId.DANGER_AREA) ? 2 : 0;
		_mask |= _player.hasCharmOfCourage() ? 4 : 0;
	}
	
	@Override
	public boolean write(PacketWriter packet)
	{
		OutgoingPackets.ETC_STATUS_UPDATE.writeId(packet);
		
		packet.writeC(_player.getCharges()); // 1-7 increase force, level
		packet.writeD(_player.getWeightPenalty()); // 1-4 weight penalty, level (1=50%, 2=66.6%, 3=80%, 4=100%)
		packet.writeC(0); // Weapon Grade Penalty [1-4]
		packet.writeC(0); // Armor Grade Penalty [1-4]
		packet.writeC(0); // Death Penalty [1-15, 0 = disabled)], not used anymore in Ertheia
		packet.writeC(_player.getChargedSouls());
		packet.writeC(_mask);
		return true;
	}
}
