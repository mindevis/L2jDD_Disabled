
package ai.areas.TalkingIsland.Walkers;

import org.l2jdd.gameserver.enums.ChatType;
import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.actor.Npc;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.network.NpcStringId;

import ai.AbstractNpcAI;

/**
 * Rotina AI.
 * @author Gladicek
 */
public class Rotina extends AbstractNpcAI
{
	// NPC
	private static final int ROTINA = 33027;
	private static final int MEI = 33280;
	// Items
	private static final int WEAPON = 15304;
	// Distances
	private static final int MIN_DISTANCE = 70;
	private static final int MAX_DISTANCE = 200;
	
	private Rotina()
	{
		addSpawnId(ROTINA);
	}
	
	@Override
	public void onTimerEvent(String event, StatSet params, Npc npc, PlayerInstance player)
	{
		switch (event)
		{
			case "NPC_SHOUT":
			{
				npc.broadcastSay(ChatType.NPC_GENERAL, NpcStringId.YOU_LL_EARN_TONS_OF_ITEMS_USING_THE_TRAINING_GROUNDS);
				getTimers().addTimer("NPC_SHOUT", (10 + getRandom(5)) * 1000, npc, null);
				break;
			}
			case "WALK_AROUND_MEI":
			{
				followNpc(npc, MEI, 115, MIN_DISTANCE, MAX_DISTANCE);
				break;
			}
		}
	}
	
	@Override
	public String onSpawn(Npc npc)
	{
		npc.setRHandId(WEAPON);
		followNpc(npc, MEI, 115, MIN_DISTANCE, MAX_DISTANCE);
		getTimers().addTimer("NPC_SHOUT", (10 + getRandom(5)) * 1000, npc, null);
		getTimers().addRepeatingTimer("WALK_AROUND_MEI", 1000, npc, null);
		return super.onSpawn(npc);
	}
	
	public static void main(String[] args)
	{
		new Rotina();
	}
}