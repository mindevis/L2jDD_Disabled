
package ai.areas.FantasyIsle.KrateisCube;

import org.l2jdd.gameserver.model.Location;
import org.l2jdd.gameserver.model.actor.Npc;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;

import ai.AbstractNpcAI;

/**
 * Kratei's Cube AI
 * @author Mobius
 */
public class KrateisCube extends AbstractNpcAI
{
	// NPC
	private static final int MANAGER = 32503; // Kratei's Cube Entrance Manager
	// Location
	private static final Location FANTASY_TELEPORT = new Location(-59193, -56893, -2034);
	
	public KrateisCube()
	{
		addStartNpc(MANAGER);
		addFirstTalkId(MANAGER);
		addTalkId(MANAGER);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, PlayerInstance player)
	{
		String htmltext = null;
		switch (event)
		{
			case "32503-1.html":
			case "32503-2.html":
			case "32503-3.html":
			{
				htmltext = event;
				break;
			}
			case "teleportToFantasyIsland":
			{
				player.teleToLocation(FANTASY_TELEPORT);
				break;
			}
		}
		return htmltext;
	}
	
	@Override
	public String onFirstTalk(Npc npc, PlayerInstance player)
	{
		return npc.getId() + ".html";
	}
	
	public static void main(String[] args)
	{
		new KrateisCube();
	}
}
