
package ai.others.DelusionTeleport;

import java.util.HashMap;
import java.util.Map;

import org.l2jdd.gameserver.model.Location;
import org.l2jdd.gameserver.model.actor.Npc;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.model.variables.PlayerVariables;

import ai.AbstractNpcAI;

/**
 * Chambers of Delusion teleport AI.
 * @author GKR
 */
public class DelusionTeleport extends AbstractNpcAI
{
	// NPCs
	private static final int[] NPCS =
	{
		32484, // Pathfinder Worker
		32658, // Guardian of Eastern Seal
		32659, // Guardian of Western Seal
		32660, // Guardian of Southern Seal
		32661, // Guardian of Northern Seal
		32662, // Guardian of Great Seal
		32663, // Guardian of Tower of Seal
	};
	// Location
	private static final Location[] HALL_LOCATIONS =
	{
		new Location(-114597, -152501, -6750),
		new Location(-114589, -154162, -6750)
	};
	
	private static final Map<Integer, Location> RETURN_LOCATIONS = new HashMap<>();
	
	static
	{
		RETURN_LOCATIONS.put(0, new Location(43835, -47749, -792)); // Undefined origin, return to Rune
		RETURN_LOCATIONS.put(20, new Location(-14023, 123677, -3112)); // Gludio
		RETURN_LOCATIONS.put(30, new Location(18101, 145936, -3088)); // Dion
		RETURN_LOCATIONS.put(40, new Location(80905, 56361, -1552)); // Heine
		RETURN_LOCATIONS.put(50, new Location(108469, 221690, -3592)); // Oren
		RETURN_LOCATIONS.put(60, new Location(85991, -142234, -1336)); // Schuttgart
		RETURN_LOCATIONS.put(70, new Location(42772, -48062, -792)); // Rune
	}
	
	private DelusionTeleport()
	{
		addStartNpc(NPCS);
		addTalkId(NPCS);
	}
	
	@Override
	public String onTalk(Npc npc, PlayerInstance player)
	{
		if (npc.getId() == NPCS[0]) // Pathfinder Worker
		{
			final int locId = npc.getParameters().getInt("Level", -1);
			player.getVariables().set(PlayerVariables.DELUSION_RETURN, RETURN_LOCATIONS.containsKey(locId) ? locId : 0);
			player.teleToLocation(getRandomEntry(HALL_LOCATIONS), false);
		}
		else
		{
			final int townId = player.getVariables().getInt(PlayerVariables.DELUSION_RETURN, 0);
			player.teleToLocation(RETURN_LOCATIONS.get(townId), true);
			player.getVariables().remove(PlayerVariables.DELUSION_RETURN);
		}
		return super.onTalk(npc, player);
	}
	
	public static void main(String[] args)
	{
		new DelusionTeleport();
	}
}