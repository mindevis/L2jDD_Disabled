
package org.l2jdd.gameserver.network.clientpackets;

import org.l2jdd.commons.network.PacketReader;
import org.l2jdd.gameserver.instancemanager.CastleManager;
import org.l2jdd.gameserver.instancemanager.ClanEntryManager;
import org.l2jdd.gameserver.instancemanager.FortManager;
import org.l2jdd.gameserver.model.World;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.model.clan.Clan;
import org.l2jdd.gameserver.network.GameClient;
import org.l2jdd.gameserver.network.SystemMessageId;
import org.l2jdd.gameserver.network.serverpackets.ExPledgeCount;
import org.l2jdd.gameserver.network.serverpackets.JoinPledge;
import org.l2jdd.gameserver.network.serverpackets.PledgeShowInfoUpdate;
import org.l2jdd.gameserver.network.serverpackets.PledgeShowMemberListAdd;
import org.l2jdd.gameserver.network.serverpackets.PledgeShowMemberListAll;
import org.l2jdd.gameserver.network.serverpackets.SystemMessage;

/**
 * @author Sdw
 */
public class RequestPledgeWaitingUserAccept implements IClientIncomingPacket
{
	private boolean _acceptRequest;
	private int _playerId;
	private int _clanId;
	
	@Override
	public boolean read(GameClient client, PacketReader packet)
	{
		_acceptRequest = packet.readD() == 1;
		_playerId = packet.readD();
		_clanId = packet.readD();
		return true;
	}
	
	@Override
	public void run(GameClient client)
	{
		final PlayerInstance player = client.getPlayer();
		if ((player == null) || (player.getClan() == null))
		{
			return;
		}
		
		if (_acceptRequest)
		{
			final PlayerInstance target = World.getInstance().getPlayer(_playerId);
			final Clan clan = player.getClan();
			if ((target != null) && (target.getClan() == null) && (clan != null))
			{
				target.sendPacket(new JoinPledge(clan.getId()));
				
				// player.setPowerGrade(9); // academy
				target.setPowerGrade(5); // New member starts at 5, not confirmed.
				clan.addClanMember(target);
				target.setClanPrivileges(target.getClan().getRankPrivs(target.getPowerGrade()));
				target.sendPacket(SystemMessageId.ENTERED_THE_CLAN);
				
				final SystemMessage sm = new SystemMessage(SystemMessageId.S1_HAS_JOINED_THE_CLAN);
				sm.addString(target.getName());
				clan.broadcastToOnlineMembers(sm);
				
				if (clan.getCastleId() > 0)
				{
					CastleManager.getInstance().getCastleByOwner(clan).giveResidentialSkills(target);
				}
				if (clan.getFortId() > 0)
				{
					FortManager.getInstance().getFortByOwner(clan).giveResidentialSkills(target);
				}
				target.sendSkillList();
				
				clan.broadcastToOtherOnlineMembers(new PledgeShowMemberListAdd(target), target);
				clan.broadcastToOnlineMembers(new PledgeShowInfoUpdate(clan));
				clan.broadcastToOnlineMembers(new ExPledgeCount(clan));
				
				// This activates the clan tab on the new member.
				PledgeShowMemberListAll.sendAllTo(target);
				target.setClanJoinExpiryTime(0);
				target.broadcastUserInfo();
				
				ClanEntryManager.getInstance().removePlayerApplication(_clanId, _playerId);
			}
		}
		else
		{
			ClanEntryManager.getInstance().removePlayerApplication(_clanId, _playerId);
		}
	}
}
