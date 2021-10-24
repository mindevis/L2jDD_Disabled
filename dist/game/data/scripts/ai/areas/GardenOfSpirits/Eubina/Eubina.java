
package ai.areas.GardenOfSpirits.Eubina;

import org.l2jdd.gameserver.model.Location;
import org.l2jdd.gameserver.model.actor.Npc;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;

import ai.AbstractNpcAI;

/**
 * @author Mobius
 */
public class Eubina extends AbstractNpcAI
{
	// NPC
	private static final int EUBINA = 34238;
	// Teleport
	private static final Location BLACKBIRD_CAMPSITE = new Location(-48354, 69435, -3081);
	
	private Eubina()
	{
		addFirstTalkId(EUBINA);
		addTalkId(EUBINA);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, PlayerInstance player)
	{
		String htmltext = null;
		switch (event)
		{
			case "teleport":
			{
				// if (player.getFactionLevel(Faction.UNWORLDLY_VISITORS) < 4)
				// {
				// 	htmltext = "34238-01.html";
				// }
				// else
				// {
					player.teleToLocation(BLACKBIRD_CAMPSITE);
				// }
				break;
			}
		}
		return htmltext;
	}
	
	@Override
	public String onFirstTalk(Npc npc, PlayerInstance player)
	{
		return "34238.html";
	}
	
	public static void main(String[] args)
	{
		new Eubina();
	}
}
