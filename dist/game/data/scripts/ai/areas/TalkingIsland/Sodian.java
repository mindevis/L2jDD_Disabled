
package ai.areas.TalkingIsland;

import org.l2jdd.gameserver.enums.ChatType;
import org.l2jdd.gameserver.model.actor.Npc;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.network.NpcStringId;

import ai.AbstractNpcAI;

/**
 * Sodian AI.
 * @author Gladicek
 */
public class Sodian extends AbstractNpcAI
{
	// NPCs
	private static final int SODIAN = 33229;
	// Misc
	private static final NpcStringId[] SODIAN_SHOUT =
	{
		NpcStringId.COME_BROWSE_OUR_INVENTORY,
		NpcStringId.INCREDIBLE_WEAPONS_FOR_SALE
	};
	
	private Sodian()
	{
		addSpawnId(SODIAN);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, PlayerInstance player)
	{
		if (event.equals("SPAM_TEXT") && (npc != null))
		{
			npc.broadcastSay(ChatType.NPC_GENERAL, SODIAN_SHOUT[getRandom(2)], 1000);
		}
		return super.onAdvEvent(event, npc, player);
	}
	
	@Override
	public String onSpawn(Npc npc)
	{
		startQuestTimer("SPAM_TEXT", 8000, npc, null, true);
		return super.onSpawn(npc);
	}
	
	public static void main(String[] args)
	{
		new Sodian();
	}
}