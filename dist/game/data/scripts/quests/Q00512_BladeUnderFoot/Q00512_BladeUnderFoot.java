
package quests.Q00512_BladeUnderFoot;

import org.l2jdd.gameserver.enums.QuestType;
import org.l2jdd.gameserver.model.actor.Npc;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.model.clan.Clan;
import org.l2jdd.gameserver.model.quest.Quest;
import org.l2jdd.gameserver.model.quest.QuestState;
import org.l2jdd.gameserver.model.siege.Castle;

/**
 * Blade Under Foot (512)
 * @author Mobius
 */
public class Q00512_BladeUnderFoot extends Quest
{
	// NPCs
	private static final int[] NPCS =
	{
		36403, // Gludio
		36404, // Dion
		36405, // Giran
		36406, // Oren
		36407, // Aden
		36408, // Innadril
		36409, // Goddard
		36410, // Rune
		36411, // Schuttgart
	};
	// Items
	private static final int MARK = 9798;
	private static final int KNIGHT_EPALUETTE = 9912;
	// Misc
	private static final int MIN_LEVEL = 90;
	
	public Q00512_BladeUnderFoot()
	{
		super(512);
		addStartNpc(NPCS);
		addTalkId(NPCS);
		addCondMinLevel(MIN_LEVEL, "Warden-00a.htm");
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, PlayerInstance player)
	{
		final QuestState qs = getQuestState(player, false);
		if (qs == null)
		{
			return null;
		}
		
		String htmltext = event;
		switch (event)
		{
			case "Warden-03.html":
			case "Warden-04.html":
			case "Warden-05.html":
			case "Warden-06.html":
			case "Warden-09.html":
			{
				break;
			}
			case "Warden-02.htm":
			{
				qs.startQuest();
				break;
			}
			case "Warden-10.html":
			{
				qs.exitQuest(QuestType.REPEATABLE);
				break;
			}
			default:
			{
				htmltext = null;
			}
		}
		return htmltext;
	}
	
	@Override
	public String onTalk(Npc npc, PlayerInstance player)
	{
		final QuestState qs = getQuestState(player, true);
		String htmltext; // = getNoQuestMsg(player);
		if (qs.isCreated())
		{
			final Castle castle = npc.getCastle();
			final Clan clan = player.getClan();
			htmltext = ((castle != null) && (clan != null) && (clan.getCastleId() == castle.getResidenceId())) ? "Warden-01.htm" : "Warden-00b.htm";
		}
		else
		{
			final long itemCount = getQuestItemsCount(player, MARK);
			if (itemCount == 0)
			{
				htmltext = "Warden-07.html";
			}
			else
			{
				takeItems(player, MARK, itemCount);
				giveItems(player, KNIGHT_EPALUETTE, itemCount * 2);
				htmltext = "Warden-08.html";
			}
		}
		return htmltext;
	}
}