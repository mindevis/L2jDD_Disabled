
package quests.Q00835_PitiableMelisa;

import org.l2jdd.Config;
import org.l2jdd.gameserver.enums.Movie;
import org.l2jdd.gameserver.enums.QuestType;
import org.l2jdd.gameserver.model.actor.Creature;
import org.l2jdd.gameserver.model.actor.Npc;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.model.quest.Quest;
import org.l2jdd.gameserver.model.quest.QuestState;
import org.l2jdd.gameserver.model.quest.State;

/**
 * Pitiable Melisa (835)
 * @URL https://l2wiki.com/Pitiable_Melisa
 * @author Gigi
 * @date 2019-02-04 - [23:59:06]
 */
public class Q00835_PitiableMelisa extends Quest
{
	// NPCs
	private static final int KANNA = 34173;
	private static final int SETTLEN = 34180;
	// Monsters
	private static final int[] MONSTERS =
	{
		23686, // Frost Glacier Golem
		23687 // Glacier Golem
	};
	// Items
	private static final int ICE_CRYSTAL_SHARD = 46594;
	private static final int FRENZED_TAUTIS_FRAGMENT = 47884;
	private static final int INSANE_KELBIMS_FRAGMENT = 47885;
	private static final int SOE_MISTYC_TAVERN = 46564;
	private static final int MYSTIC_ARMOR_PIACE = 46587;
	
	public Q00835_PitiableMelisa()
	{
		super(835);
		addStartNpc(KANNA);
		addTalkId(SETTLEN);
		addSeeCreatureId(KANNA);
		addKillId(MONSTERS);
		registerQuestItems(ICE_CRYSTAL_SHARD);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, PlayerInstance player)
	{
		String htmltext = null;
		final QuestState qs = getQuestState(player, false);
		switch (event)
		{
			case "NOTIFY_Q835":
			{
				qs.setCond(qs.getCond() + 1, true);
				break;
			}
			case "34180-02.html":
			{
				final int chance = getRandom(100);
				if (chance <= 10)
				{
					giveItems(player, FRENZED_TAUTIS_FRAGMENT, 1);
				}
				else if ((chance > 10) && (chance <= 20))
				{
					giveItems(player, INSANE_KELBIMS_FRAGMENT, 1);
				}
				else if ((chance > 20) && (chance <= 50))
				{
					giveItems(player, MYSTIC_ARMOR_PIACE, 1);
				}
				else
				{
					giveItems(player, SOE_MISTYC_TAVERN, 1);
				}
				addExpAndSp(player, 6_362_541_900L, 15_270_101);
				qs.exitQuest(QuestType.REPEATABLE, true);
				htmltext = event;
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
			case State.STARTED:
			{
				if (qs.isCond(5))
				{
					htmltext = "34180-01.html";
				}
			}
		}
		return htmltext;
	}
	
	@Override
	public String onSeeCreature(Npc npc, Creature creature, boolean isSummon)
	{
		final PlayerInstance player = creature.getActingPlayer();
		if (player != null)
		{
			final QuestState qs = getQuestState(player, true);
			if (!qs.isStarted())
			{
				playMovie(player, Movie.EPIC_FREYA_SLIDE);
				qs.startQuest();
			}
		}
		return super.onSeeCreature(npc, creature, isSummon);
	}
	
	@Override
	public String onKill(Npc npc, PlayerInstance player, boolean isSummon)
	{
		executeForEachPlayer(player, npc, isSummon, true, false);
		return super.onKill(npc, player, isSummon);
	}
	
	@Override
	public void actionForEachPlayer(PlayerInstance player, Npc npc, boolean isSummon)
	{
		final QuestState qs = getQuestState(player, false);
		if ((qs != null) && qs.isCond(2) && player.isInsideRadius3D(npc, Config.ALT_PARTY_RANGE) && giveItemRandomly(player, npc, ICE_CRYSTAL_SHARD, 1, 10, 1.0, true))
		{
			qs.setCond(3, true);
		}
	}
}
