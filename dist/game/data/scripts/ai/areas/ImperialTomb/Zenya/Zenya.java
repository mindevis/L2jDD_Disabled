
package ai.areas.ImperialTomb.Zenya;

import org.l2jdd.gameserver.model.Location;
import org.l2jdd.gameserver.model.actor.Npc;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;

import ai.AbstractNpcAI;

/**
 * Zenya AI.
 * @author Stayway
 */
public class Zenya extends AbstractNpcAI
{
	// NPC
	private static final int ZENYA = 32140;
	// Location
	private static final Location IMPERIAL_TOMB = new Location(183400, -81208, -5323);
	// Misc
	private static final int MIN_LEVEL = 80;
	
	private Zenya()
	{
		addStartNpc(ZENYA);
		addFirstTalkId(ZENYA);
		addTalkId(ZENYA);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, PlayerInstance player)
	{
		String htmltext = null;
		switch (event)
		{
			case "32140.html":
			case "32140-1.html":
			case "32140-2.html":
			case "32140-4.html":
			{
				htmltext = event;
				break;
			}
			case "teleport":
			{
				player.teleToLocation(IMPERIAL_TOMB);
				break;
			}
		}
		return htmltext;
	}
	
	@Override
	public String onFirstTalk(Npc npc, PlayerInstance player)
	{
		return player.getLevel() < MIN_LEVEL ? "32140-3.html" : "32140.html";
	}
	
	public static void main(String[] args)
	{
		new Zenya();
	}
}