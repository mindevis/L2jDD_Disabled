
package ai.areas.AteliaFortress.TeleportDevice;

import org.l2jdd.gameserver.enums.Faction;
import org.l2jdd.gameserver.model.Location;
import org.l2jdd.gameserver.model.actor.Npc;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;

import ai.AbstractNpcAI;

/**
 * Kingdom's Royal Guard Teleport Device
 * @author Gigi
 * @date 2018-04-30 - [23:32:48]
 */
public class TeleportDevice extends AbstractNpcAI
{
	// NPC
	private static final int TELEPORT_DEVICE = 34242;
	// Teleport's
	private static final Location LOCATION1 = new Location(-46335, 59575, -2960);
	private static final Location LOCATION2 = new Location(-42307, 51232, -2032);
	private static final Location LOCATION3 = new Location(-44060, 40139, -1432);
	private static final Location LOCATION4 = new Location(-57242, 43811, -1552);
	
	private TeleportDevice()
	{
		addFirstTalkId(TELEPORT_DEVICE);
		addTalkId(TELEPORT_DEVICE);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, PlayerInstance player)
	{
		if (player.getFactionLevel(Faction.KINGDOM_ROYAL_GUARDS) < 3)
		{
			return "34242-01.html";
		}
		switch (event)
		{
			case "teleport1":
			{
				player.teleToLocation(LOCATION1);
				break;
			}
			case "teleport2":
			{
				player.teleToLocation(LOCATION2);
				break;
			}
			case "teleport3":
			{
				player.teleToLocation(LOCATION3);
				break;
			}
			case "teleport4":
			{
				player.teleToLocation(LOCATION4);
				break;
			}
		}
		return null;
	}
	
	@Override
	public String onFirstTalk(Npc npc, PlayerInstance player)
	{
		return "34242.html";
	}
	
	public static void main(String[] args)
	{
		new TeleportDevice();
	}
}
