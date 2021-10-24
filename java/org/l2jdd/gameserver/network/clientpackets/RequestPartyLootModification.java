
package org.l2jdd.gameserver.network.clientpackets;

import org.l2jdd.commons.network.PacketReader;
import org.l2jdd.gameserver.enums.PartyDistributionType;
import org.l2jdd.gameserver.model.Party;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.network.GameClient;

/**
 * @author JIV
 */
public class RequestPartyLootModification implements IClientIncomingPacket
{
	private int _partyDistributionTypeId;
	
	@Override
	public boolean read(GameClient client, PacketReader packet)
	{
		_partyDistributionTypeId = packet.readD();
		return true;
	}
	
	@Override
	public void run(GameClient client)
	{
		final PlayerInstance player = client.getPlayer();
		if (player == null)
		{
			return;
		}
		
		final PartyDistributionType partyDistributionType = PartyDistributionType.findById(_partyDistributionTypeId);
		if (partyDistributionType == null)
		{
			return;
		}
		
		final Party party = player.getParty();
		if ((party == null) || !party.isLeader(player) || (partyDistributionType == party.getDistributionType()))
		{
			return;
		}
		party.requestLootChange(partyDistributionType);
	}
}
