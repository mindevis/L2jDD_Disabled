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
package quests.Q10589_WhereFatesIntersect;

import java.util.HashSet;
import java.util.Set;

import org.l2jdd.gameserver.enums.QuestType;
import org.l2jdd.gameserver.model.Location;
import org.l2jdd.gameserver.model.actor.Npc;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.model.holders.ItemHolder;
import org.l2jdd.gameserver.model.holders.NpcLogListHolder;
import org.l2jdd.gameserver.model.quest.Quest;
import org.l2jdd.gameserver.model.quest.QuestState;
import org.l2jdd.gameserver.model.quest.State;
import org.l2jdd.gameserver.network.NpcStringId;
import org.l2jdd.gameserver.network.serverpackets.ExTutorialShowId;

/**
 * Where Fates Intersect (10589)
 * @URL https://l2wiki.com/Where_Fates_Intersect
 * @author NightBR
 */
public class Q10589_WhereFatesIntersect extends Quest
{
	// NPCs
	private static final int TARTI = 34505;
	private static final int HERPHAH = 34362;
	private static final int VOLLODOS = 30137;
	private static final int JOACHIM = 34513;
	private static final int[] MONSTERS =
	{
		24452, // Doom Soldier
		24453, // Doom Servant
		24454, // Doom Berserker
		24455, // Doom Seer
	};
	// Item
	private static final int MONSTER_DROP = 80853; // Undead Blood
	private static final ItemHolder SOE_HERPHAH = new ItemHolder(80857, 1); // Scroll of Escape: Herphah
	// Misc
	private static final int REQUIRED_DROP_COUNT = 200;
	private static final int KILLING_NPCSTRING_ID1 = NpcStringId.LV_85_WHERE_FATES_INTERSECT_IN_PROGRESS.getId();
	private static final int KILLING_NPCSTRING_ID2 = NpcStringId.LV_85_WHERE_FATES_INTERSECT_2.getId();
	private static final int REACH_LV_95 = NpcStringId.REACH_LV_95.getId();
	private static final QuestType QUEST_TYPE = QuestType.ONE_TIME; // REPEATABLE, ONE_TIME, DAILY
	private static final boolean PARTY_QUEST = true;
	private static final int KILLING_COND = 3;
	private static final int FINISH_COND = 4;
	private static final int MIN_LEVEL = 85;
	// Rewards
	private static final int REWARD_ITEM1 = 80908; // Lv. 95 Achievement Reward Box
	private static final int REWARD_ITEM1_AMOUNT = 1;
	// Location
	private static final Location TOWN_OF_ADEN = new Location(146568, 26808, -2208);
	private static final Location ALTAR_OF_EVIL = new Location(-14088, 22168, -3626);
	
	public Q10589_WhereFatesIntersect()
	{
		super(10589);
		addStartNpc(TARTI);
		addTalkId(TARTI, HERPHAH, VOLLODOS, JOACHIM);
		addKillId(MONSTERS);
		registerQuestItems(MONSTER_DROP);
		addCondMinLevel(MIN_LEVEL, getNoQuestMsg(null));
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, PlayerInstance player)
	{
		final QuestState qs = getQuestState(player, false);
		if (qs == null)
		{
			return null;
		}
		
		String htmltext = null;
		switch (event)
		{
			case "34505-01.htm":
			case "34505-02.htm":
			case "34505-04.html":
			case "34362-02.html":
			case "34362-05.html":
			case "30137-02.html":
			case "34513-02.html":
			{
				htmltext = event;
				break;
			}
			case "34505-03.htm":
			{
				// Show Service/Help/View Map page
				player.sendPacket(new ExTutorialShowId(12));
				qs.startQuest();
				htmltext = event;
				break;
			}
			case "34362-03.html":
			{
				// Show Service/Help/Adventure's Guide page
				player.sendPacket(new ExTutorialShowId(25));
				qs.setCond(2, true);
				htmltext = event;
				break;
			}
			case "34362-06.html":
			{
				if (qs.isCond(4))
				{
					// Check player level it must be 95+
					qs.setCond(5, true);
					htmltext = event;
				}
				break;
			}
			case "30137-03.html":
			{
				qs.setCond(3, true);
				htmltext = event;
				break;
			}
			case "34513-03.html":
			{
				if (qs.isCond(5))
				{
					takeItems(player, MONSTER_DROP, -1);
					// Reward.
					rewardItems(player, REWARD_ITEM1, REWARD_ITEM1_AMOUNT);
					qs.exitQuest(QUEST_TYPE, true);
				}
				break;
			}
			case "townofaden":
			{
				player.teleToLocation(TOWN_OF_ADEN); // Town of Aden near Npc Herphah
				break;
			}
			case "altarofevil":
			{
				player.teleToLocation(ALTAR_OF_EVIL); // Altar of Evil near Npc Vollodos
				break;
			}
		}
		return htmltext;
	}
	
	@Override
	public String onTalk(Npc npc, PlayerInstance player)
	{
		final QuestState qs = getQuestState(player, true);
		String htmltext = getNoQuestMsg(player);
		switch (qs.getState())
		{
			case State.CREATED:
			{
				htmltext = "34505-00.htm";
				break;
			}
			case State.STARTED:
			{
				switch (npc.getId())
				{
					case TARTI:
					{
						if (qs.isCond(1))
						{
							htmltext = "34505-04.html";
						}
						break;
					}
					case HERPHAH:
					{
						if (qs.isCond(1))
						{
							htmltext = "34362-01.html";
						}
						else if (qs.isCond(2))
						{
							htmltext = "34362-07.html";
						}
						else if (qs.isCond(4))
						{
							htmltext = "34362-04.html";
						}
						else if (qs.isCond(5))
						{
							htmltext = "34362-06.html";
						}
						break;
					}
					case VOLLODOS:
					{
						if (qs.isCond(2))
						{
							htmltext = "30137-01.html";
						}
						else if (qs.isCond(3))
						{
							htmltext = "30137-04.html";
						}
						break;
					}
					case JOACHIM:
					{
						if (qs.isCond(5))
						{
							htmltext = "34513-01.html";
							break;
						}
					}
				}
				break;
			}
			case State.COMPLETED:
			{
				if (qs.isNowAvailable())
				{
					qs.setState(State.CREATED);
					htmltext = "34505-00.htm";
				}
				else
				{
					htmltext = getAlreadyCompletedMsg(player, QUEST_TYPE);
				}
				break;
			}
		}
		return htmltext;
	}
	
	@Override
	public String onKill(Npc npc, PlayerInstance killer, boolean isSummon)
	{
		final QuestState qs = PARTY_QUEST ? getRandomPartyMemberState(killer, -1, 3, npc) : getQuestState(killer, false);
		if ((qs != null) && qs.isCond(KILLING_COND))
		{
			final PlayerInstance player = qs.getPlayer();
			giveItemRandomly(player, npc, MONSTER_DROP, 1, REQUIRED_DROP_COUNT, 1, true);
			if ((getQuestItemsCount(player, MONSTER_DROP) >= REQUIRED_DROP_COUNT) && (player.getLevel() >= 95))
			{
				giveItems(killer, SOE_HERPHAH);
				qs.setCond(FINISH_COND, true);
			}
			
			sendNpcLogList(player);
		}
		
		return super.onKill(npc, killer, isSummon);
	}
	
	@Override
	public Set<NpcLogListHolder> getNpcLogList(PlayerInstance player)
	{
		final QuestState qs = getQuestState(player, false);
		if ((qs != null) && qs.isCond(KILLING_COND))
		{
			final Set<NpcLogListHolder> holder = new HashSet<>();
			if (player.getLevel() >= 95)
			{
				holder.add(new NpcLogListHolder(REACH_LV_95, true, 1));
			}
			holder.add(new NpcLogListHolder(KILLING_NPCSTRING_ID1, true, (int) getQuestItemsCount(player, MONSTER_DROP)));
			holder.add(new NpcLogListHolder(KILLING_NPCSTRING_ID2, true, (int) getQuestItemsCount(player, MONSTER_DROP)));
			return holder;
		}
		return super.getNpcLogList(player);
	}
}
