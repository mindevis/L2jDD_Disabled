
package ai.bosses.Istina.Rumiese;

import org.l2jdd.gameserver.model.actor.Npc;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;

import ai.AbstractNpcAI;

/**
 * Rumiese AI.
 * @author St3eT
 */
public class Rumiese extends AbstractNpcAI
{
	// NPC
	private static final int RUMIESE = 33151;
	// Item
	private static final int CONTROL_DEVICE = 17608; // Energy Control Device
	
	public Rumiese()
	{
		addStartNpc(RUMIESE);
		addTalkId(RUMIESE);
		addFirstTalkId(RUMIESE);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, PlayerInstance player)
	{
		String htmltext = null;
		switch (event)
		{
			case "33151-01.html":
			{
				htmltext = event;
				break;
			}
			case "giveDevice":
			{
				if (!hasQuestItems(player, CONTROL_DEVICE))
				{
					giveItems(player, CONTROL_DEVICE, 1);
					htmltext = "33151-02.html";
				}
				else
				{
					htmltext = "33151-03.html";
				}
				break;
			}
		}
		return htmltext;
	}
	
	public static void main(String[] args)
	{
		new Rumiese();
	}
}