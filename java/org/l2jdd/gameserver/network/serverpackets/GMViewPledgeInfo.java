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
package org.l2jdd.gameserver.network.serverpackets;

import org.l2jdd.commons.network.PacketWriter;
import org.l2jdd.gameserver.data.sql.ClanTable;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.model.clan.Clan;
import org.l2jdd.gameserver.model.clan.ClanMember;
import org.l2jdd.gameserver.network.OutgoingPackets;

/**
 * format SdSS dddddddd d (Sddddd)
 * @version $Revision: 1.1.2.1.2.3 $ $Date: 2005/03/27 15:29:57 $
 */
public class GMViewPledgeInfo implements IClientOutgoingPacket
{
	private final Clan _clan;
	private final PlayerInstance _player;
	
	public GMViewPledgeInfo(Clan clan, PlayerInstance player)
	{
		_clan = clan;
		_player = player;
	}
	
	@Override
	public boolean write(PacketWriter packet)
	{
		final int TOP = ClanTable.getInstance().getTopRate(_clan.getClanId());
		OutgoingPackets.GM_VIEW_PLEDGE_INFO.writeId(packet);
		packet.writeS(_player.getName());
		packet.writeD(_clan.getClanId());
		packet.writeD(0x00);
		packet.writeS(_clan.getName());
		packet.writeS(_clan.getLeaderName());
		packet.writeD(_clan.getCrestId()); // -> no, it's no longer used (nuocnam) fix by game
		packet.writeD(_clan.getLevel());
		packet.writeD(_clan.getCastleId());
		packet.writeD(_clan.getHideoutId());
		packet.writeD(TOP);
		packet.writeD(_clan.getReputationScore());
		packet.writeD(0);
		packet.writeD(0);
		
		packet.writeD(_clan.getAllyId()); // c2
		packet.writeS(_clan.getAllyName()); // c2
		packet.writeD(_clan.getAllyCrestId()); // c2
		packet.writeD(_clan.isAtWar()); // c3
		
		final ClanMember[] members = _clan.getMembers();
		packet.writeD(members.length);
		
		for (ClanMember member : members)
		{
			packet.writeS(member.getName());
			packet.writeD(member.getLevel());
			packet.writeD(member.getClassId());
			packet.writeD(0);
			packet.writeD(1);
			packet.writeD(member.isOnline() ? member.getObjectId() : 0);
			packet.writeD(0);
		}
		return true;
	}
}
