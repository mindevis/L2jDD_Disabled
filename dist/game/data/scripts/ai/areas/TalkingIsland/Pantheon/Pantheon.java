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
package ai.areas.TalkingIsland.Pantheon;

import org.l2jdd.gameserver.enums.ChatType;
import org.l2jdd.gameserver.model.Location;
import org.l2jdd.gameserver.model.actor.Npc;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.network.NpcStringId;

import ai.AbstractNpcAI;

/**
 * Pantheon AI.
 * @author Gladicek
 */
public class Pantheon extends AbstractNpcAI
{
	// NPC
	private static final int PANTHEON = 32972;
	// Location
	private static final Location MUSEUM = new Location(-114711, 243911, -7968);
	// Misc
	private static final int MIN_LEVEL = 20;
	
	private Pantheon()
	{
		addSpawnId(PANTHEON);
		addStartNpc(PANTHEON);
		addFirstTalkId(PANTHEON);
		addTalkId(PANTHEON);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, PlayerInstance player)
	{
		String htmltext = null;
		switch (event)
		{
			case "32972-1.html":
			{
				htmltext = event;
				break;
			}
			case "teleport_museum":
			{
				if (player.getLevel() < MIN_LEVEL)
				{
					htmltext = "32972-noteleport.html";
				}
				else
				{
					player.teleToLocation(MUSEUM);
				}
				break;
			}
			case "TEXT_SPAM":
			{
				npc.broadcastSay(ChatType.NPC_GENERAL, NpcStringId.IS_IT_BETTER_TO_END_DESTINY_OR_START_DESTINY);
				break;
			}
		}
		return htmltext;
	}
	
	@Override
	public String onSpawn(Npc npc)
	{
		startQuestTimer("TEXT_SPAM", 10000, npc, null, true);
		return super.onSpawn(npc);
	}
	
	public static void main(String[] args)
	{
		new Pantheon();
	}
}