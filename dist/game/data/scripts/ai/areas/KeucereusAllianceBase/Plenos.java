
package ai.areas.KeucereusAllianceBase;

import org.l2jdd.gameserver.enums.ChatType;
import org.l2jdd.gameserver.model.actor.Npc;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.network.NpcStringId;

import ai.AbstractNpcAI;

/**
 * Plenos AI.
 * @author St3eT
 */
public class Plenos extends AbstractNpcAI
{
	// NPCs
	private static final int PLENOS = 32563;
	
	private Plenos()
	{
		addSpawnId(PLENOS);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, PlayerInstance player)
	{
		if (event.equals("SPAM_TEXT") && (npc != null))
		{
			npc.broadcastSay(ChatType.NPC_GENERAL, NpcStringId.YOU_CAN_BE_TELEPORTED_TO_EACH_SEED_IF_YOU_VOLUNTEER_WHY_NOT_TRY, 1000);
		}
		return super.onAdvEvent(event, npc, player);
	}
	
	@Override
	public String onSpawn(Npc npc)
	{
		startQuestTimer("SPAM_TEXT", 10000, npc, null, true);
		return super.onSpawn(npc);
	}
	
	public static void main(String[] args)
	{
		new Plenos();
	}
}