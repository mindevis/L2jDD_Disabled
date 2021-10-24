
package quests.Q10801_TheDimensionalWarpPart1;

import java.util.HashSet;
import java.util.Set;

import org.l2jdd.gameserver.enums.QuestSound;
import org.l2jdd.gameserver.model.Party;
import org.l2jdd.gameserver.model.actor.Npc;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.model.holders.NpcLogListHolder;
import org.l2jdd.gameserver.model.quest.Quest;
import org.l2jdd.gameserver.model.quest.QuestState;
import org.l2jdd.gameserver.model.quest.State;

/**
 * The Dimensional Warp, Part 1 (10801)
 * @URL https://l2wiki.com/The_Dimensional_Warp,_Part_1
 * @author Gigi
 * @date 2018-09-01 - [12:20:59]
 */
public class Q10801_TheDimensionalWarpPart1 extends Quest
{
	// NPC
	private static final int RESED = 33974;
	// Monsters
	private static final int DIMENSIONAL_BUGBEAR = 23465;
	// Others
	private static final int MIN_LEVEL = 99;
	private static final int DIMENSIONAL_BRACELET_STAGE_1 = 39747;
	private static final int WARP_CRYSTAL = 39597;
	
	public Q10801_TheDimensionalWarpPart1()
	{
		super(10801);
		addStartNpc(RESED);
		addTalkId(RESED);
		addKillId(DIMENSIONAL_BUGBEAR);
		addCondMinLevel(MIN_LEVEL, "33974-00.htm");
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, PlayerInstance player)
	{
		String htmltext = null;
		final QuestState qs = getQuestState(player, false);
		if (qs == null)
		{
			return htmltext;
		}
		
		switch (event)
		{
			case "33974-02.htm":
			case "33974-03.htm":
			{
				htmltext = event;
				break;
			}
			case "33974-04.htm":
			{
				qs.startQuest();
				htmltext = event;
				break;
			}
			case "33974-07.html":
			{
				if (qs.isCond(2))
				{
					if (player.getLevel() >= MIN_LEVEL)
					{
						addExpAndSp(player, 44442855977L, 0);
						giveItems(player, DIMENSIONAL_BRACELET_STAGE_1, 1);
						giveItems(player, WARP_CRYSTAL, 300);
						qs.exitQuest(false, true);
						htmltext = event;
					}
					else
					{
						htmltext = getNoQuestLevelRewardMsg(player);
					}
				}
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
				htmltext = "33974-01.htm";
				break;
			}
			case State.STARTED:
			{
				htmltext = (qs.isCond(1)) ? "33974-05.html" : "33974-06.html";
				break;
			}
			case State.COMPLETED:
			{
				htmltext = getAlreadyCompletedMsg(player);
				break;
			}
		}
		return htmltext;
	}
	
	@Override
	public String onKill(Npc npc, PlayerInstance killer, boolean isSummon)
	{
		final Party party = killer.getParty();
		if (party != null)
		{
			party.getMembers().forEach(p -> onKill(npc, p));
		}
		else
		{
			onKill(npc, killer);
		}
		return super.onKill(npc, killer, isSummon);
	}
	
	private void onKill(Npc npc, PlayerInstance killer)
	{
		final QuestState qs = getRandomPartyMemberState(killer, 1, 3, npc);
		if (qs != null)
		{
			final PlayerInstance player = qs.getPlayer();
			int kills = qs.getInt("killed_" + DIMENSIONAL_BUGBEAR);
			if (kills < 100)
			{
				qs.set("killed_" + DIMENSIONAL_BUGBEAR, ++kills);
				playSound(player, QuestSound.ITEMSOUND_QUEST_ITEMGET);
			}
			if (kills >= 100)
			{
				qs.setCond(2, true);
			}
			sendNpcLogList(player);
		}
	}
	
	@Override
	public Set<NpcLogListHolder> getNpcLogList(PlayerInstance player)
	{
		final QuestState qs = getQuestState(player, false);
		if ((qs != null) && qs.isCond(1))
		{
			final Set<NpcLogListHolder> holder = new HashSet<>();
			holder.add(new NpcLogListHolder(DIMENSIONAL_BUGBEAR, false, qs.getInt("killed_" + DIMENSIONAL_BUGBEAR)));
			return holder;
		}
		return super.getNpcLogList(player);
	}
}
