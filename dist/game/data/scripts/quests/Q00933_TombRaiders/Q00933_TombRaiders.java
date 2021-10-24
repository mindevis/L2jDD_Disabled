
package quests.Q00933_TombRaiders;

import java.util.HashSet;
import java.util.Set;

import org.l2jdd.gameserver.enums.QuestSound;
import org.l2jdd.gameserver.model.actor.Npc;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.model.holders.NpcLogListHolder;
import org.l2jdd.gameserver.model.quest.Quest;
import org.l2jdd.gameserver.model.quest.QuestState;
import org.l2jdd.gameserver.model.quest.State;
import org.l2jdd.gameserver.network.NpcStringId;

/**
 * Tomb Raiders (933)
 * @URL https://l2wiki.com/Tomb_Raiders
 * @author Sero
 */
public class Q00933_TombRaiders extends Quest
{
	// NPCs
	private static final int SEARCH_TEAM_TELEPORTER = 34552;
	private static final int LEOPARD = 32594;
	// Monsters
	private static final int TOMB_GUARDIAN = 24580;
	private static final int TOMB_RAIDER = 24581;
	private static final int TOMB_WATCHER = 24584;
	private static final int TOMB_SOULTAKER = 24583;
	private static final int TOMB_PATROL = 24582;
	// Item
	private static final int BENUSTA_REWARD_BOX = 81151;
	// Misc
	private static final String KILL_COUNT_VAR = "KillCount";
	
	public Q00933_TombRaiders()
	{
		super(933);
		addStartNpc(SEARCH_TEAM_TELEPORTER);
		addTalkId(SEARCH_TEAM_TELEPORTER, LEOPARD);
		addKillId(TOMB_GUARDIAN, TOMB_RAIDER, TOMB_WATCHER, TOMB_SOULTAKER, TOMB_PATROL);
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
			case "34552-02.htm":
			{
				qs.startQuest();
				htmltext = event;
				
				break;
			}
			case "32594-04.htm":
			{
				if (qs.isCond(2))
				{
					addExpAndSp(player, 20700253956096L, 1450359376);
					giveItems(player, BENUSTA_REWARD_BOX, 1);
					qs.exitQuest(true, true);
					htmltext = event;
				}
				break;
			}
		}
		return htmltext;
	}
	
	@Override
	public String onTalk(Npc npc, PlayerInstance player)
	{
		String htmltext = getNoQuestMsg(player);
		final QuestState qs = getQuestState(player, true);
		switch (qs.getState())
		{
			case State.CREATED:
			{
				htmltext = "34552-01.htm";
				break;
			}
			case State.STARTED:
			{
				if (qs.isCond(2))
				{
					htmltext = "32594-03.htm";
				}
				else
				{
					htmltext = "32594-06.htm";
				}
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
		final QuestState qs = getQuestState(killer, false);
		if ((qs != null) && qs.isCond(1))
		{
			final int killCount = qs.getInt(KILL_COUNT_VAR);
			if (killCount < 150)
			{
				qs.set(KILL_COUNT_VAR, killCount + 1);
				playSound(killer, QuestSound.ITEMSOUND_QUEST_ITEMGET);
				sendNpcLogList(killer);
			}
			else
			{
				qs.setCond(2);
				qs.unset(KILL_COUNT_VAR);
			}
		}
		return super.onKill(npc, killer, isSummon);
	}
	
	// TODO:
	// public String onEnterZone(Creature creature, ZoneType zone)
	
	@Override
	public Set<NpcLogListHolder> getNpcLogList(PlayerInstance player)
	{
		final QuestState qs = getQuestState(player, false);
		if ((qs != null) && qs.isCond(1))
		{
			final Set<NpcLogListHolder> holder = new HashSet<>();
			holder.add(new NpcLogListHolder(NpcStringId.DEFEAT_MONSTERS_IN_THE_IMPERIAL_TOMB.getId(), true, qs.getInt(KILL_COUNT_VAR)));
			return holder;
		}
		return super.getNpcLogList(player);
	}
}
