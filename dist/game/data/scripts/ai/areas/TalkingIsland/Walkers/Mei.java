
package ai.areas.TalkingIsland.Walkers;

import org.l2jdd.gameserver.enums.ChatType;
import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.actor.Npc;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.network.NpcStringId;

import ai.AbstractNpcAI;

/**
 * Mei AI.
 * @author Gladicek
 */
public class Mei extends AbstractNpcAI
{
	// NPC
	private static final int MEI = 33280;
	private static final int ROTINA = 33027;
	
	private Mei()
	{
		addSpawnId(MEI);
	}
	
	@Override
	public void onTimerEvent(String event, StatSet params, Npc npc, PlayerInstance player)
	{
		if (event.equals("NPC_SHOUT"))
		{
			npc.broadcastSay(ChatType.NPC_GENERAL, NpcStringId.IF_YOU_IGNORE_THE_TRAINING_GROUNDS_YOU_LL_REGRET_IT);
			getTimers().addTimer("NPC_SHOUT", (10 + getRandom(5)) * 1000, npc, null);
		}
		else if (event.equals("NPC_FOLLOW"))
		{
			addSpawn(ROTINA, npc.getX() + 10, npc.getY() + 10, npc.getZ() + 10, 0, false, 0);
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
		new Mei();
	}
}