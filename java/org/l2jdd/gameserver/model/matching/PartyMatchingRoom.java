
package org.l2jdd.gameserver.model.matching;

import org.l2jdd.gameserver.enums.MatchingMemberType;
import org.l2jdd.gameserver.enums.MatchingRoomType;
import org.l2jdd.gameserver.enums.PartyMatchingRoomLevelType;
import org.l2jdd.gameserver.enums.UserInfoType;
import org.l2jdd.gameserver.instancemanager.MatchingRoomManager;
import org.l2jdd.gameserver.model.Party;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.network.SystemMessageId;
import org.l2jdd.gameserver.network.serverpackets.ExClosePartyRoom;
import org.l2jdd.gameserver.network.serverpackets.ExPartyRoomMember;
import org.l2jdd.gameserver.network.serverpackets.ListPartyWaiting;
import org.l2jdd.gameserver.network.serverpackets.PartyRoomInfo;
import org.l2jdd.gameserver.network.serverpackets.SystemMessage;

/**
 * @author Sdw
 */
public class PartyMatchingRoom extends MatchingRoom
{
	public PartyMatchingRoom(String title, int loot, int minLevel, int maxLevel, int maxmem, PlayerInstance leader)
	{
		super(title, loot, minLevel, maxLevel, maxmem, leader);
	}
	
	@Override
	protected void onRoomCreation(PlayerInstance player)
	{
		player.broadcastUserInfo(UserInfoType.CLAN);
		player.sendPacket(new ListPartyWaiting(PartyMatchingRoomLevelType.ALL, -1, 1, player.getLevel()));
		player.sendPacket(SystemMessageId.YOU_HAVE_CREATED_A_PARTY_ROOM);
	}
	
	@Override
	protected void notifyInvalidCondition(PlayerInstance player)
	{
		player.sendPacket(SystemMessageId.YOU_DO_NOT_MEET_THE_REQUIREMENTS_TO_ENTER_THAT_PARTY_ROOM);
	}
	
	@Override
	protected void notifyNewMember(PlayerInstance player)
	{
		// Update other players
		for (PlayerInstance member : getMembers())
		{
			if (member != player)
			{
				member.sendPacket(new ExPartyRoomMember(member, this));
			}
		}
		
		// Send SystemMessage to other players
		final SystemMessage sm = new SystemMessage(SystemMessageId.C1_HAS_ENTERED_THE_PARTY_ROOM);
		sm.addPcName(player);
		for (PlayerInstance member : getMembers())
		{
			if (member != player)
			{
				sm.sendTo(member);
			}
		}
		
		// Update new player
		player.sendPacket(new PartyRoomInfo(this));
		player.sendPacket(new ExPartyRoomMember(player, this));
	}
	
	@Override
	protected void notifyRemovedMember(PlayerInstance player, boolean kicked, boolean leaderChanged)
	{
		final SystemMessage sm = new SystemMessage(kicked ? SystemMessageId.C1_HAS_BEEN_KICKED_FROM_THE_PARTY_ROOM : SystemMessageId.C1_HAS_LEFT_THE_PARTY_ROOM);
		sm.addPcName(player);
		
		getMembers().forEach(p ->
		{
			p.sendPacket(new PartyRoomInfo(this));
			p.sendPacket(new ExPartyRoomMember(player, this));
			p.sendPacket(sm);
			p.sendPacket(SystemMessageId.THE_LEADER_OF_THE_PARTY_ROOM_HAS_CHANGED);
		});
		
		player.sendPacket(new SystemMessage(kicked ? SystemMessageId.YOU_HAVE_BEEN_OUSTED_FROM_THE_PARTY_ROOM : SystemMessageId.YOU_HAVE_EXITED_THE_PARTY_ROOM));
		player.sendPacket(ExClosePartyRoom.STATIC_PACKET);
	}
	
	@Override
	public void disbandRoom()
	{
		getMembers().forEach(p ->
		{
			p.sendPacket(SystemMessageId.THE_PARTY_ROOM_HAS_BEEN_DISBANDED);
			p.sendPacket(ExClosePartyRoom.STATIC_PACKET);
			p.setMatchingRoom(null);
			p.broadcastUserInfo(UserInfoType.CLAN);
			MatchingRoomManager.getInstance().addToWaitingList(p);
		});
		
		getMembers().clear();
		
		MatchingRoomManager.getInstance().removeMatchingRoom(this);
	}
	
	@Override
	public MatchingRoomType getRoomType()
	{
		return MatchingRoomType.PARTY;
	}
	
	@Override
	public MatchingMemberType getMemberType(PlayerInstance player)
	{
		if (isLeader(player))
		{
			return MatchingMemberType.PARTY_LEADER;
		}
		
		final Party leaderParty = getLeader().getParty();
		final Party playerParty = player.getParty();
		if ((leaderParty != null) && (playerParty != null) && (playerParty == leaderParty))
		{
			return MatchingMemberType.PARTY_MEMBER;
		}
		
		return MatchingMemberType.WAITING_PLAYER;
	}
}
