
package ai.bosses.Freya.Jinia;

import org.l2jdd.gameserver.model.actor.Npc;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;

import ai.AbstractNpcAI;

/**
 * Jinia AI.
 * @author Adry_85
 */
public class Jinia extends AbstractNpcAI
{
	// NPC
	private static final int JINIA = 32781;
	// Items
	private static final int FROZEN_CORE = 15469;
	private static final int BLACK_FROZEN_CORE = 15470;
	// Misc
	private static final int MIN_LEVEL = 82;
	
	private Jinia()
	{
		addStartNpc(JINIA);
		addFirstTalkId(JINIA);
		addTalkId(JINIA);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, PlayerInstance player)
	{
		String htmltext = event;
		switch (event)
		{
			case "check":
			{
				if (hasAtLeastOneQuestItem(player, FROZEN_CORE, BLACK_FROZEN_CORE))
				{
					htmltext = "32781-03.html";
				}
				else
				{
					// final QuestState qs = player.getQuestState(Q10286_ReunionWithSirra.class.getSimpleName());
					// if ((qs != null) && qs.isCompleted())
					// {
					// giveItems(player, FROZEN_CORE, 1);
					// }
					// else
					// {
					giveItems(player, BLACK_FROZEN_CORE, 1);
					// }
					htmltext = "32781-04.html";
				}
				break;
			}
		}
		return htmltext;
	}
	
	@Override
	public String onFirstTalk(Npc npc, PlayerInstance player)
	{
		// final QuestState qs = player.getQuestState(Q10286_ReunionWithSirra.class.getSimpleName());
		// if ((qs != null) && (player.getLevel() >= MIN_LEVEL))
		// {
		// if (qs.isCond(5) || qs.isCond(6))
		// {
		// return "32781-09.html";
		// }
		// else if (qs.isCond(7))
		// {
		// return "32781-01.html";
		// }
		// }
		// return "32781-02.html";
		if (player.getLevel() >= MIN_LEVEL)
		{
			return "32781-01.html";
		}
		return "32781-02.html";
	}
	
	public static void main(String[] args)
	{
		new Jinia();
	}
}