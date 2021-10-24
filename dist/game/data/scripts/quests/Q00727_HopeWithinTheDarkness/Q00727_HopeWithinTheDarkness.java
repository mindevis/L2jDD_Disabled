
package quests.Q00727_HopeWithinTheDarkness;

import org.l2jdd.gameserver.enums.QuestType;
import org.l2jdd.gameserver.model.actor.Npc;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.model.clan.Clan;
import org.l2jdd.gameserver.model.quest.Quest;
import org.l2jdd.gameserver.model.quest.QuestState;
import org.l2jdd.gameserver.model.siege.Castle;

/**
 * Hope within the Darkness (727)
 * @author Mobius
 */
public class Q00727_HopeWithinTheDarkness extends Quest
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
	private static final int KNIGHT_EPALUETTE = 9912;
	// Misc
	private static final int MIN_LEVEL = 90;
	
	public Q00727_HopeWithinTheDarkness()
	{
		super(727);
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
			{
				break;
			}
			case "Warden-02.htm":
			{
				qs.startQuest();
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
		String htmltext = getNoQuestMsg(player);
		if (qs.isCreated())
		{
			final Castle castle = npc.getCastle();
			final Clan clan = player.getClan();
			htmltext = ((castle != null) && (clan != null) && (clan.getCastleId() == castle.getResidenceId())) ? "Warden-01.htm" : "Warden-00b.htm";
		}
		else if (qs.isStarted())
		{
			if (qs.isCond(1))
			{
				htmltext = "Warden-03.html";
			}
			else
			{
				player.setPkKills(Math.max(0, player.getPkKills() - 1));
				giveItems(player, KNIGHT_EPALUETTE, 300);
				qs.exitQuest(QuestType.REPEATABLE);
				htmltext = "Warden-05.html";
			}
		}
		return htmltext;
	}
}
