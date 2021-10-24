
package ai.areas.RuinsOfAgony;

import org.l2jdd.gameserver.enums.ChatType;
import org.l2jdd.gameserver.model.actor.Npc;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.network.NpcStringId;

import ai.AbstractNpcAI;

/**
 * Guard AI.
 * @author St3eT
 */
public class RoAGuard extends AbstractNpcAI
{
	// NPCs
	private static final int GUARD = 33427;
	
	private RoAGuard()
	{
		addSpawnId(GUARD);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, PlayerInstance player)
	{
		if (event.equals("NPC_SHOUT"))
		{
			npc.broadcastSay(ChatType.NPC_GENERAL, NpcStringId.THIS_IS_RUINS_OF_AGONY_WHERE_KANILOV_IS);
			startQuestTimer("NPC_SHOUT", (10 + getRandom(5)) * 1000, npc, null);
		}
		return super.onAdvEvent(event, npc, player);
	}
	
	@Override
	public String onSpawn(Npc npc)
	{
		startQuestTimer("NPC_SHOUT", (10 + getRandom(5)) * 1000, npc, null);
		return super.onSpawn(npc);
	}
	
	public static void main(String[] args)
	{
		new RoAGuard();
	}
}