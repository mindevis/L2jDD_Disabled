
package ai.areas.TalkingIsland.Walkers;

import org.l2jdd.gameserver.enums.ChatType;
import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.actor.Npc;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.network.NpcStringId;

import ai.AbstractNpcAI;

/**
 * Allada AI.
 * @author Gladicek
 */
public class Allada extends AbstractNpcAI
{
	// NPC
	private static final int RINNE = 33234;
	private static final int ALLADA = 33220;
	// Items
	private static final int WEAPON = 15304;
	// Distances
	private static final int MIN_DISTANCE = 70;
	private static final int MAX_DISTANCE = 200;
	
	private Allada()
	{
		addSpawnId(ALLADA);
	}
	
	@Override
	public void onTimerEvent(String event, StatSet params, Npc npc, PlayerInstance player)
	{
		switch (event)
		{
			case "NPC_SHOUT":
			{
				npc.broadcastSay(ChatType.NPC_GENERAL, NpcStringId.IT_S_A_MIRACLE_THAT_THE_TALKING_ISLAND_HAS_RESTORED);
				getTimers().addTimer("NPC_SHOUT", (10 + getRandom(5)) * 1000, npc, null);
				break;
			}
			case "WALK_AROUND_RINNE":
			{
				followNpc(npc, RINNE, 115, MIN_DISTANCE, MAX_DISTANCE);
				break;
			}
		}
	}
	
	@Override
	public String onSpawn(Npc npc)
	{
		npc.setRHandId(WEAPON);
		followNpc(npc, RINNE, 115, MIN_DISTANCE, MAX_DISTANCE);
		getTimers().addTimer("NPC_SHOUT", (10 + getRandom(5)) * 1000, npc, null);
		getTimers().addRepeatingTimer("WALK_AROUND_RINNE", 1000, npc, null);
		return super.onSpawn(npc);
	}
	
	public static void main(String[] args)
	{
		new Allada();
	}
}