
package ai.areas.DragonValley.MercenaryTeleport;

import java.util.HashMap;
import java.util.Map;

import org.l2jdd.gameserver.model.Location;
import org.l2jdd.gameserver.model.actor.Npc;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;

import ai.AbstractNpcAI;

/**
 * Mercenary and Mercenary Captain teleport AI.
 * @author Gigi
 */
public class MercenaryTeleport extends AbstractNpcAI
{
	// NPCs
	private static final int NAMO = 33973;
	private static final int MERCENARY = 33971;
	private static final int MERCENARY_CAPTAIN = 33970;
	// Locations
	private static final Map<String, Location> LOCATIONS = new HashMap<>();
	static
	{
		// Captain
		LOCATIONS.put("NorthernDragonValley", new Location(87712, 106060, -3176));
		LOCATIONS.put("SouthernDragonValley", new Location(88016, 118852, -3056));
		LOCATIONS.put("NorthernWhirlingVortex", new Location(108064, 112432, -3008));
		LOCATIONS.put("SouthernWhirlingVortex", new Location(109918, 121266, -3720));
		LOCATIONS.put("DeepInWhirlingVortex", new Location(119506, 112331, -3688));
		LOCATIONS.put("EntranceToAntharasLair", new Location(131116, 114333, -3704));
		LOCATIONS.put("AntharasLairBarrierBridge", new Location(146129, 111232, -3568));
		LOCATIONS.put("DeepInAntharasLair", new Location(148447, 110582, -3944));
		// Mercenary
		LOCATIONS.put("TownOfGiran", new Location(83497, 148015, -3400));
		LOCATIONS.put("DragonValleyJunction", new Location(80012, 115911, -3672));
		LOCATIONS.put("WhirlingVortexJunction", new Location(102278, 113038, -3720));
	}
	
	private MercenaryTeleport()
	{
		addStartNpc(NAMO, MERCENARY, MERCENARY_CAPTAIN);
		addFirstTalkId(NAMO, MERCENARY, MERCENARY_CAPTAIN);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, PlayerInstance player)
	{
		player.teleToLocation(LOCATIONS.get(event), true);
		return super.onAdvEvent(event, npc, player);
	}
	
	@Override
	public String onFirstTalk(Npc npc, PlayerInstance player)
	{
		return npc.getId() + ".html";
	}
	
	public static void main(String[] args)
	{
		new MercenaryTeleport();
	}
}