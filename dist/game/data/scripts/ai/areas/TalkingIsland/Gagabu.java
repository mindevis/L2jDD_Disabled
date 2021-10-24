
package ai.areas.TalkingIsland;

import org.l2jdd.gameserver.enums.ChatType;
import org.l2jdd.gameserver.model.actor.Npc;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.network.NpcStringId;

import ai.AbstractNpcAI;

/**
 * Gagabu AI.
 * @author Gladicek
 */
public class Gagabu extends AbstractNpcAI
{
	// NPCs
	private static final int GAGABU = 33284;
	// Misc
	private static final NpcStringId[] GAGABU_SHOUT =
	{
		NpcStringId.SPIRITSHOTS_ARE_MIXED,
		NpcStringId.WHEN_WILL_I_ORGANIZE_THIS_ALL
	};
	
	private Gagabu()
	{
		addSpawnId(GAGABU);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, PlayerInstance player)
	{
		if (event.equals("SPAM_TEXT") && (npc != null))
		{
			npc.broadcastSay(ChatType.NPC_GENERAL, GAGABU_SHOUT[getRandom(2)], 1000);
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
		new Gagabu();
	}
}