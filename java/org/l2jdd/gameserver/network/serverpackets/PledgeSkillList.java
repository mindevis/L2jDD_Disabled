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
import org.l2jdd.gameserver.model.Skill;
import org.l2jdd.gameserver.model.clan.Clan;
import org.l2jdd.gameserver.network.OutgoingPackets;

/**
 * Format: (ch) d [dd].
 * @author -Wooden-
 */
public class PledgeSkillList implements IClientOutgoingPacket
{
	private final Skill[] _skills;
	
	/**
	 * Instantiates a new pledge skill list.
	 * @param clan the clan
	 */
	public PledgeSkillList(Clan clan)
	{
		_skills = clan.getAllSkills();
	}
	
	@Override
	public boolean write(PacketWriter packet)
	{
		OutgoingPackets.PLEDGE_SKILL_LIST.writeId(packet);
		packet.writeD(_skills.length);
		for (Skill sk : _skills)
		{
			packet.writeD(sk.getId());
			packet.writeD(sk.getLevel());
		}
		return true;
	}
}