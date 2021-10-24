
package ai.areas.AncientCityArcan.Lykus;

import org.l2jdd.gameserver.model.actor.Npc;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.model.itemcontainer.Inventory;

import ai.AbstractNpcAI;

/**
 * Lykus AI.
 * @author St3eT
 */
public class Lykus extends AbstractNpcAI
{
	// NPCs
	private static final int LYKUS = 33521;
	// Items
	private static final int POLISHED_SHIELD = 17723; // Polished Ancient Hero's Shield
	private static final int OLD_SHIELD = 17724; // Orbis Ancient Hero's Shield
	
	public Lykus()
	{
		addFirstTalkId(LYKUS);
		addTalkId(LYKUS);
		addStartNpc(LYKUS);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, PlayerInstance player)
	{
		String htmltext = null;
		switch (event)
		{
			case "33521-01.html":
			case "33521-02.html":
			case "33521-03.html":
			case "33521-04.html":
			case "33521-05.html":
			case "33521-07.html":
			case "33521-08.html":
			case "33521-12.html":
			{
				htmltext = event;
				break;
			}
			default:
			{
				if (event.startsWith("trade"))
				{
					final int count = (int) (event.equals("trade1") ? 1 : getQuestItemsCount(player, OLD_SHIELD));
					if (!hasAtLeastOneQuestItem(player, OLD_SHIELD))
					{
						htmltext = "33521-11.html";
					}
					else if (player.getAdena() < (5000 * count))
					{
						htmltext = "33521-10.html";
					}
					else
					{
						takeItems(player, Inventory.ADENA_ID, 5000 * count);
						takeItems(player, OLD_SHIELD, count);
						giveItems(player, POLISHED_SHIELD, count);
						htmltext = "33521-09.html";
					}
				}
			}
		}
		return htmltext;
	}
	
	public static void main(String[] args)
	{
		new Lykus();
	}
}
