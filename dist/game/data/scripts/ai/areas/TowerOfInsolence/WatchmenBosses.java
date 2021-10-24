
package ai.areas.TowerOfInsolence;

import org.l2jdd.gameserver.model.Location;
import org.l2jdd.gameserver.model.actor.Npc;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;

import ai.AbstractNpcAI;

/**
 * @author Mobius
 */
public class WatchmenBosses extends AbstractNpcAI
{
	// NPCs
	private static final int WATCHMAN_OF_THE_FORGOTTEN = 24555;
	private static final int WATCHMAN_OF_THE_RESURRECTED = 24561;
	private static final int WATCHMAN_OF_THE_CURSED = 24567;
	// Locations
	private static final Location[] WATCHMAN_OF_THE_FORGOTTEN_LOCATIONS =
	{
		new Location(-86568, 19304, -15429),
		new Location(-82024, 20440, -15426),
		new Location(-79624, 18488, -15429),
		new Location(-77560, 16184, -15426),
		new Location(-77656, 12776, -15429),
		new Location(-82024, 11848, -15426),
		new Location(-80152, 14328, -15429),
		new Location(-83864, 18136, -15429),
		new Location(-83976, 14296, -15429),
	};
	private static final Location[] WATCHMAN_OF_THE_RESURRECTED_LOCATIONS =
	{
		new Location(-83944, 17960, -12933),
		new Location(-80120, 14184, -12933),
		new Location(-80168, 17992, -12933),
		new Location(-83816, 14296, -12933),
		new Location(-86504, 16168, -12929),
		new Location(-85912, 12088, -12933),
		new Location(-82024, 11720, -12929),
		new Location(-80088, 10936, -12933),
		new Location(-77560, 16152, -12929),
	};
	private static final Location[] WATCHMAN_OF_THE_CURSED_LOCATIONS =
	{
		new Location(-77673, 16163, -10306),
		new Location(-79384, 18344, -10309),
		new Location(-82024, 20584, -10306),
		new Location(-84344, 18552, -10309),
		new Location(-86360, 16168, -10306),
		new Location(-86104, 12888, -10309),
		new Location(-83736, 10792, -10309),
		new Location(-83448, 14792, -10306),
		new Location(-80088, 17336, -10309),
		new Location(-84216, 17208, -10309),
		new Location(-80904, 13976, -10309),
	};
	
	private WatchmenBosses()
	{
		addKillId(WATCHMAN_OF_THE_FORGOTTEN, WATCHMAN_OF_THE_RESURRECTED, WATCHMAN_OF_THE_CURSED);
		
		startQuestTimer("SPAWN_WATCHMAN_OF_THE_FORGOTTEN", 1000, null, null);
		startQuestTimer("SPAWN_WATCHMAN_OF_THE_RESURRECTED", 1000, null, null);
		startQuestTimer("SPAWN_WATCHMAN_OF_THE_CURSED", 1000, null, null);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, PlayerInstance player)
	{
		switch (event)
		{
			case "SPAWN_WATCHMAN_OF_THE_FORGOTTEN":
			{
				addSpawn(WATCHMAN_OF_THE_FORGOTTEN, getRandomEntry(WATCHMAN_OF_THE_FORGOTTEN_LOCATIONS));
				break;
			}
			case "SPAWN_WATCHMAN_OF_THE_RESURRECTED":
			{
				addSpawn(WATCHMAN_OF_THE_RESURRECTED, getRandomEntry(WATCHMAN_OF_THE_RESURRECTED_LOCATIONS));
				break;
			}
			case "SPAWN_WATCHMAN_OF_THE_CURSED":
			{
				addSpawn(WATCHMAN_OF_THE_CURSED, getRandomEntry(WATCHMAN_OF_THE_CURSED_LOCATIONS));
				break;
			}
		}
		return null;
	}
	
	@Override
	public String onKill(Npc npc, PlayerInstance killer, boolean isSummon)
	{
		switch (npc.getId())
		{
			case WATCHMAN_OF_THE_FORGOTTEN:
			{
				startQuestTimer("SPAWN_WATCHMAN_OF_THE_FORGOTTEN", 28800000, null, null);
				break;
			}
			case WATCHMAN_OF_THE_RESURRECTED:
			{
				startQuestTimer("SPAWN_WATCHMAN_OF_THE_RESURRECTED", 28800000, null, null);
				break;
			}
			case WATCHMAN_OF_THE_CURSED:
			{
				startQuestTimer("SPAWN_WATCHMAN_OF_THE_CURSED", 28800000, null, null);
				break;
			}
		}
		return super.onKill(npc, killer, isSummon);
	}
	
	public static void main(String[] args)
	{
		new WatchmenBosses();
	}
}
