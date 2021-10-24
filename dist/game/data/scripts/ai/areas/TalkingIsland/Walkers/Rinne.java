
package ai.areas.TalkingIsland.Walkers;

import org.l2jdd.gameserver.enums.ChatType;
import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.actor.Npc;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.network.NpcStringId;

import ai.AbstractNpcAI;

/**
 * Rinne AI.
 * @author Gladicek
 */
public class Rinne extends AbstractNpcAI
{
	// NPC
	private static final int RINNE = 33234;
	private static final int ALLADA = 33220;
	// Items
	private static final int WEAPON = 15302;
	
	private Rinne()
	{
		addSpawnId(RINNE);
	}
	
	@Override
	public void onTimerEvent(String event, StatSet params, Npc npc, PlayerInstance player)
	{
		switch (event)
		{
			case "TID_LEFTWALK":
			{
				npc.setRHandId(WEAPON);
				getTimers().addTimer("TID_STRAIGHTWALK", (5000 + getRandom(5)) * 1000, npc, null);
				break;
			}
			case "TID_STRAIGHTWALK":
			{
				npc.setRHandId(0);
				getTimers().addTimer("TID_LEFTWALK", (5000 + getRandom(5)) * 1000, npc, null);
				break;
			}
			case "NPC_SHOUT":
			{
				npc.broadcastSay(ChatType.NPC_GENERAL, NpcStringId.ALL_RACES_CAME_TOGETHER_TO_REBUILD_TALKING_ISLAND_VILLAGE);
				getTimers().addTimer("NPC_SHOUT", (10 + getRandom(5)) * 1000, npc, null);
				break;
			}
			case "NPC_FOLLOW":
			{
				addSpawn(ALLADA, npc.getX() + 10, npc.getY() + 10, npc.getZ(), 0, false, 0);
				break;
			}
		}
	}
	
	@Override
	public String onSpawn(Npc npc)
	{
		getTimers().addTimer("TID_LEFTWALK", (5000 + getRandom(5)) * 1000, npc, null);
		getTimers().addTimer("NPC_SHOUT", (10 + getRandom(5)) * 1000, npc, null);
		getTimers().addTimer("NPC_FOLLOW", 100, npc, null);
		return super.onSpawn(npc);
	}
	
	public static void main(String[] args)
	{
		new Rinne();
	}
}