
package ai.others.TeleportToFantasy;

import java.util.HashMap;
import java.util.Map;

import org.l2jdd.gameserver.enums.ChatType;
import org.l2jdd.gameserver.model.Location;
import org.l2jdd.gameserver.model.actor.Npc;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.network.NpcStringId;

import ai.AbstractNpcAI;

/**
 * Fantasy Island teleport AI.
 * @author Mobius
 */
public class TeleportToFantasy extends AbstractNpcAI
{
	// NPC
	private static final int PADDIES = 32378;
	// Locations
	private static final Location[] ISLE_LOCATIONS =
	{
		new Location(-58752, -56898, -2032),
		new Location(-59716, -57868, -2032),
		new Location(-60691, -56893, -2032),
		new Location(-59720, -55921, -2032)
	};
	private static final Map<Integer, Location> TELEPORTER_LOCATIONS = new HashMap<>();
	static
	{
		TELEPORTER_LOCATIONS.put(30320, new Location(-80826, 149775, -3043)); // Richlin
		TELEPORTER_LOCATIONS.put(30256, new Location(-12672, 122776, -3116)); // Bella
		TELEPORTER_LOCATIONS.put(30059, new Location(15670, 142983, -2705)); // Trisha
		TELEPORTER_LOCATIONS.put(30080, new Location(83400, 147943, -3404)); // Clarissa
		TELEPORTER_LOCATIONS.put(30899, new Location(111409, 219364, -3545)); // Flauen
		TELEPORTER_LOCATIONS.put(30177, new Location(82956, 53162, -1495)); // Valentina
		TELEPORTER_LOCATIONS.put(30848, new Location(146331, 25762, -2018)); // Elisa
		TELEPORTER_LOCATIONS.put(30233, new Location(116819, 76994, -2714)); // Esmeralda
		TELEPORTER_LOCATIONS.put(31320, new Location(43835, -47749, -792)); // Ilyana
		TELEPORTER_LOCATIONS.put(31275, new Location(147930, -55281, -2728)); // Tatiana
		TELEPORTER_LOCATIONS.put(31964, new Location(87386, -143246, -1293)); // Bilia
	}
	// Other
	private static final String FANTASY_RETURN = "FANTASY_RETURN";
	
	private TeleportToFantasy()
	{
		addStartNpc(PADDIES);
		addStartNpc(TELEPORTER_LOCATIONS.keySet());
		addTalkId(PADDIES);
		addTalkId(TELEPORTER_LOCATIONS.keySet());
	}
	
	@Override
	public String onTalk(Npc npc, PlayerInstance player)
	{
		if (npc.getId() == PADDIES)
		{
			final int returnId = player.getVariables().getInt(FANTASY_RETURN, -1);
			if (returnId > 30000) // Old script compatibility.
			{
				player.teleToLocation(TELEPORTER_LOCATIONS.get(returnId));
				player.getVariables().remove(FANTASY_RETURN);
			}
			else
			{
				npc.broadcastSay(ChatType.GENERAL, NpcStringId.IF_YOUR_MEANS_OF_ARRIVAL_WAS_A_BIT_UNCONVENTIONAL_THEN_I_LL_BE_SENDING_YOU_BACK_TO_THE_TOWN_OF_RUNE_WHICH_IS_THE_NEAREST_TOWN);
				player.teleToLocation(TELEPORTER_LOCATIONS.get(31320)); // Rune
			}
		}
		else
		{
			player.teleToLocation(getRandomEntry(ISLE_LOCATIONS));
			player.getVariables().set(FANTASY_RETURN, npc.getId());
		}
		return super.onTalk(npc, player);
	}
	
	public static void main(String[] args)
	{
		new TeleportToFantasy();
	}
}
