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

import java.util.List;

import org.l2jdd.commons.network.PacketWriter;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.model.quest.Quest;
import org.l2jdd.gameserver.model.quest.QuestState;
import org.l2jdd.gameserver.network.OutgoingPackets;

/**
 * Sh (dd) h (dddd)
 * @author Tempy
 */
public class GMViewQuestList implements IClientOutgoingPacket
{
	private final PlayerInstance _player;
	
	public GMViewQuestList(PlayerInstance player)
	{
		_player = player;
	}
	
	@Override
	public boolean write(PacketWriter packet)
	{
		OutgoingPackets.GM_VIEW_QUEST_LIST.writeId(packet);
		packet.writeS(_player.getName());
		
		final List<Quest> questList = _player.getAllActiveQuests();
		packet.writeH(questList.size()); // quest count
		for (Quest q : questList)
		{
			packet.writeD(q.getQuestId());
			final QuestState qs = _player.getQuestState(q.getName());
			if (qs == null)
			{
				packet.writeD(0);
				continue;
			}
			
			packet.writeD(qs.getInt("cond")); // stage of quest progress
		}
		return true;
	}
}
