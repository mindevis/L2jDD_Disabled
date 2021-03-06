
package ai.others.NornilTeleportDevice;

import org.l2jdd.gameserver.model.Location;
import org.l2jdd.gameserver.model.actor.Npc;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;

import ai.AbstractNpcAI;

/**
 * Nornil Teleport Device AI.
 * @author St3eT
 */
public class NornilTeleportDevice extends AbstractNpcAI
{
	// NPCs
	private static final int DEVICE = 33790; // Nornil Teleport Device
	// Locations
	private static final Location[] LOCATIONS =
	{
		new Location(-79667, 54028, -4824),
		new Location(-87483, 54024, -4440),
		new Location(-87839, 49803, -4344),
		new Location(-84995, 50974, -4600),
		new Location(-86945, 42814, -2656),
	};
	
	public NornilTeleportDevice()
	{
		addStartNpc(DEVICE);
		addFirstTalkId(DEVICE);
		addTalkId(DEVICE);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, PlayerInstance player)
	{
		if (event.startsWith("teleport_"))
		{
			final int locId = Integer.parseInt(event.replace("teleport_", ""));
			player.teleToLocation(LOCATIONS[locId - 1]);
		}
		return super.onAdvEvent(event, npc, player);
	}
	
	@Override
	public String onFirstTalk(Npc npc, PlayerInstance player)
	{
		return "NornilTeleportDevice-" + npc.getParameters().getInt("device_place", 0) + ".html";
	}
	
	public static void main(String[] args)
	{
		new NornilTeleportDevice();
	}
}