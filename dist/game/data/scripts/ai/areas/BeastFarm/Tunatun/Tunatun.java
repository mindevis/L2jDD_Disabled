
package ai.areas.BeastFarm.Tunatun;

import org.l2jdd.gameserver.model.actor.Npc;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;

import ai.AbstractNpcAI;

/**
 * Beast Herder Tunatun AI.
 * @author Adry_85
 */
public class Tunatun extends AbstractNpcAI
{
	// NPC
	private static final int TUNATUN = 31537;
	// Item
	private static final int BEAST_HANDLERS_WHIP = 15473;
	// Misc
	private static final int MIN_LEVEL = 82;
	
	private Tunatun()
	{
		addStartNpc(TUNATUN);
		addFirstTalkId(TUNATUN);
		addTalkId(TUNATUN);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, PlayerInstance player)
	{
		String htmltext = getNoQuestMsg(player);
		switch (event)
		{
			case "31537-04.html":
			case "31537-05.html":
			case "31537-06.html":
			{
				htmltext = event;
				break;
			}
			case "whip":
			{
				{
					if (!hasQuestItems(player, BEAST_HANDLERS_WHIP))
					{
						if (player.getLevel() >= MIN_LEVEL)
						{
							giveItems(player, BEAST_HANDLERS_WHIP, 1);
							htmltext = "31537-03.html";
						}
						else
						{
							htmltext = "31537-02.html";
						}
					}
					else
					{
						htmltext = "31537-01.html";
					}
				}
				break;
			}
		}
		return htmltext;
	}
	
	public static void main(String[] args)
	{
		new Tunatun();
	}
}