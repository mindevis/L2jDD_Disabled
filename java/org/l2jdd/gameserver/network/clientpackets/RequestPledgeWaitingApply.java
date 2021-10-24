/*
 * This file is part of the L2J Mobius project.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.l2jdd.gameserver.network.clientpackets;

import org.l2jdd.commons.network.PacketReader;
import org.l2jdd.gameserver.data.sql.ClanTable;
import org.l2jdd.gameserver.enums.ClanEntryStatus;
import org.l2jdd.gameserver.instancemanager.ClanEntryManager;
import org.l2jdd.gameserver.model.World;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.model.clan.Clan;
import org.l2jdd.gameserver.model.clan.entry.PledgeApplicantInfo;
import org.l2jdd.gameserver.network.GameClient;
import org.l2jdd.gameserver.network.SystemMessageId;
import org.l2jdd.gameserver.network.serverpackets.ExPledgeRecruitApplyInfo;
import org.l2jdd.gameserver.network.serverpackets.ExPledgeWaitingListAlarm;
import org.l2jdd.gameserver.network.serverpackets.SystemMessage;

/**
 * @author Sdw
 */
public class RequestPledgeWaitingApply implements IClientIncomingPacket
{
	private int _karma;
	private int _clanId;
	private String _message;
	
	@Override
	public boolean read(GameClient client, PacketReader packet)
	{
		_karma = packet.readD();
		_clanId = packet.readD();
		_message = packet.readS();
		return true;
	}
	
	@Override
	public void run(GameClient client)
	{
		final PlayerInstance player = client.getPlayer();
		if ((player == null) || (player.getClan() != null))
		{
			return;
		}
		
		final Clan clan = ClanTable.getInstance().getClan(_clanId);
		if (clan == null)
		{
			return;
		}
		
		final PledgeApplicantInfo info = new PledgeApplicantInfo(player.getObjectId(), player.getName(), player.getLevel(), _karma, _clanId, _message);
		if (ClanEntryManager.getInstance().addPlayerApplicationToClan(_clanId, info))
		{
			client.sendPacket(new ExPledgeRecruitApplyInfo(ClanEntryStatus.WAITING));
			
			final PlayerInstance clanLeader = World.getInstance().getPlayer(clan.getLeaderId());
			if (clanLeader != null)
			{
				clanLeader.sendPacket(ExPledgeWaitingListAlarm.STATIC_PACKET);
			}
		}
		else
		{
			final SystemMessage sm = new SystemMessage(SystemMessageId.YOU_MAY_APPLY_FOR_ENTRY_AFTER_S1_MINUTE_S_DUE_TO_CANCELLING_YOUR_APPLICATION);
			sm.addLong(ClanEntryManager.getInstance().getPlayerLockTime(player.getObjectId()));
			client.sendPacket(sm);
		}
	}
}
