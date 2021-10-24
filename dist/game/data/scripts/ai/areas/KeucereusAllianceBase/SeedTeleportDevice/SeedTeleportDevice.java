
package ai.areas.KeucereusAllianceBase.SeedTeleportDevice;

import org.l2jdd.gameserver.model.Location;
import org.l2jdd.gameserver.model.actor.Npc;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;

import ai.AbstractNpcAI;

/**
 * Seed Teleport Device AI.
 * @author St3eT
 */
public class SeedTeleportDevice extends AbstractNpcAI
{
	// NPCs
	private static final int SEED_TELEPORT_DEVICE = 15929;
	// Locations
	private static final Location SOA = new Location(-175572, 154572, 2712);
	private static final Location SOD = new Location(-247024, 251794, 4336);
	private static final Location SOI = new Location(-213699, 210686, 4408);
	private static final Location SOH = new Location(-147354, 152581, -14048);
	// Misc
	private static final int SOH_MIN_LV = 95;
	
	private SeedTeleportDevice()
	{
		addStartNpc(SEED_TELEPORT_DEVICE);
		addFirstTalkId(SEED_TELEPORT_DEVICE);
		addTalkId(SEED_TELEPORT_DEVICE);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, PlayerInstance player)
	{
		switch (event)
		{
			case "seedOfAnnihilation":
			{
				player.teleToLocation(SOA);
				break;
			}
			case "seedOfDestruction":
			{
				player.teleToLocation(SOD);
				break;
			}
			case "seedOfInfinity":
			{
				player.teleToLocation(SOI);
				break;
			}
			case "seedOfHellfire":
			{
				if (player.getLevel() < SOH_MIN_LV)
				{
					return "SeedOfHellfire-noLv.html";
				}
				player.teleToLocation(SOH);
				break;
			}
		}
		return super.onAdvEvent(event, npc, player);
	}
	
	public static void main(String[] args)
	{
		new SeedTeleportDevice();
	}
}