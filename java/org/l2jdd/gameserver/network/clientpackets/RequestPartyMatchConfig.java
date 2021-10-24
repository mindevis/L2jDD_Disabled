
package org.l2jdd.gameserver.network.clientpackets;

import org.l2jdd.commons.network.PacketReader;
import org.l2jdd.gameserver.enums.PartyMatchingRoomLevelType;
import org.l2jdd.gameserver.instancemanager.MatchingRoomManager;
import org.l2jdd.gameserver.model.CommandChannel;
import org.l2jdd.gameserver.model.Party;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.model.matching.CommandChannelMatchingRoom;
import org.l2jdd.gameserver.network.GameClient;
import org.l2jdd.gameserver.network.SystemMessageId;
import org.l2jdd.gameserver.network.serverpackets.ListPartyWaiting;

public class RequestPartyMatchConfig implements IClientIncomingPacket
{
	private int _page;
	private int _location;
	private PartyMatchingRoomLevelType _type;
	
	@Override
	public boolean read(GameClient client, PacketReader packet)
	{
		_page = packet.readD();
		_location = packet.readD();
		_type = packet.readD() == 0 ? PartyMatchingRoomLevelType.MY_LEVEL_RANGE : PartyMatchingRoomLevelType.ALL;
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
		
		final Party party = player.getParty();
		final CommandChannel cc = party == null ? null : party.getCommandChannel();
		if ((party != null) && (cc != null) && (cc.getLeader() == player))
		{
			if (player.getMatchingRoom() == null)
			{
				player.setMatchingRoom(new CommandChannelMatchingRoom(player.getName(), party.getDistributionType().ordinal(), 1, player.getLevel(), 50, player));
			}
		}
		else if ((cc != null) && (cc.getLeader() != player))
		{
			player.sendPacket(SystemMessageId.THE_COMMAND_CHANNEL_AFFILIATED_PARTY_S_PARTY_MEMBER_CANNOT_USE_THE_MATCHING_SCREEN);
		}
		else if ((party != null) && (party.getLeader() != player))
		{
			player.sendPacket(SystemMessageId.THE_LIST_OF_PARTY_ROOMS_CAN_ONLY_BE_VIEWED_BY_A_PERSON_WHO_IS_NOT_PART_OF_A_PARTY);
		}
		else
		{
			MatchingRoomManager.getInstance().addToWaitingList(player);
			player.sendPacket(new ListPartyWaiting(_type, _location, _page, player.getLevel()));
		}
	}
}
