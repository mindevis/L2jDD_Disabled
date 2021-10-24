
package ai.areas.TalkingIsland;

import org.l2jdd.gameserver.enums.ChatType;
import org.l2jdd.gameserver.model.actor.Npc;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.network.NpcStringId;

import ai.AbstractNpcAI;

/**
 * Stiller AI.
 * @author Gladicek
 */
public class Stiller extends AbstractNpcAI
{
	// NPCs
	private static final int STILLER = 33125;
	// Misc
	private static final NpcStringId[] STILLER_SHOUT =
	{
		NpcStringId.HEY_DID_YOU_SPEAK_WITH_PANTHEON,
		NpcStringId.EVERYONE_NEEDS_TO_MEET_PANTHEON_FIRST_BEFORE_HUNTING
	};
	
	private Stiller()
	{
		addSpawnId(STILLER);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, PlayerInstance player)
	{
		if (event.equals("SPAM_TEXT") && (npc != null))
		{
			npc.broadcastSay(ChatType.NPC_GENERAL, STILLER_SHOUT[getRandom(2)], 1000);
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
		new Stiller();
	}
}