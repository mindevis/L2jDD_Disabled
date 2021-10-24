
package ai.areas.TalkingIsland.Walkers;

import org.l2jdd.gameserver.enums.ChatType;
import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.actor.Npc;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.network.NpcStringId;

import ai.AbstractNpcAI;

/**
 * Soros AI.
 * @author Gladicek
 */
public class Soros extends AbstractNpcAI
{
	// NPC
	private static final int SOROS = 33218;
	private static final int REMONS = 33570;
	
	private Soros()
	{
		addSpawnId(SOROS);
	}
	
	@Override
	public void onTimerEvent(String event, StatSet params, Npc npc, PlayerInstance player)
	{
		switch (event)
		{
			case "NPC_SHOUT":
			{
				npc.broadcastSay(ChatType.NPC_GENERAL, NpcStringId.SOMETHING_LIKE_THAT_COMES_OUT_OF_THE_RUINS);
				getTimers().addTimer("NPC_SHOUT", (10 + getRandom(5)) * 1000, npc, null);
				break;
			}
			case "NPC_FOLLOW":
			{
				addSpawn(REMONS, npc.getX() + 10, npc.getY() + 10, npc.getZ() + 10, 0, false, 0);
				break;
			}
		}
	}
	
	@Override
	public String onSpawn(Npc npc)
	{
		getTimers().addTimer("NPC_SHOUT", (10 + getRandom(5)) * 1000, npc, null);
		getTimers().addTimer("NPC_FOLLOW", 100, npc, null);
		return super.onSpawn(npc);
	}
	
	public static void main(String[] args)
	{
		new Soros();
	}
}