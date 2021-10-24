
package org.l2jdd.gameserver.network.serverpackets;

import org.l2jdd.commons.network.PacketWriter;
import org.l2jdd.gameserver.network.OutgoingPackets;

/**
 * @author KenM
 */
public class ExDuelEnd implements IClientOutgoingPacket
{
	public static final ExDuelEnd PLAYER_DUEL = new ExDuelEnd(false);
	public static final ExDuelEnd PARTY_DUEL = new ExDuelEnd(true);
	
	private final int _partyDuel;
	
	public ExDuelEnd(boolean isPartyDuel)
	{
		_partyDuel = isPartyDuel ? 1 : 0;
	}
	
	@Override
	public boolean write(PacketWriter packet)
	{
		OutgoingPackets.EX_DUEL_END.writeId(packet);
		
		packet.writeD(_partyDuel);
		return true;
	}
}
