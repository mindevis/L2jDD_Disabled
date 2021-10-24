
package ai.areas.TalkingIsland;

import org.l2jdd.gameserver.enums.ChatType;
import org.l2jdd.gameserver.model.actor.Npc;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.network.NpcStringId;

import ai.AbstractNpcAI;

/**
 * Holly AI.
 * @author Gladicek
 */
public class Holly extends AbstractNpcAI
{
	// NPCs
	private static final int HOLLY = 33219;
	
	private Holly()
	{
		addSpawnId(HOLLY);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, PlayerInstance player)
	{
		if (event.equals("SPAM_TEXT") && (npc != null))
		{
			npc.broadcastSay(ChatType.NPC_GENERAL, NpcStringId.GIRAN_SHUTTLE_DOES_NOT_COME_ANYMORE_IT_S_ALL_IN_THE_PAST, 1000);
		}
		else if (event.equals("SOCIAL_ACTION") && (npc != null))
		{
			npc.broadcastSocialAction(6);
		}
		return super.onAdvEvent(event, npc, player);
	}
	
	@Override
	public String onSpawn(Npc npc)
	{
		npc.setRandomAnimation(false);
		startQuestTimer("SPAM_TEXT", 10000, npc, null, true);
		startQuestTimer("SOCIAL_ACTION", 2000, npc, null, true);
		return super.onSpawn(npc);
	}
	
	public static void main(String[] args)
	{
		new Holly();
	}
}