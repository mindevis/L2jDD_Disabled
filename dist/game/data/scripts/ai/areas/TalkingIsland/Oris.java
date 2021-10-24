
package ai.areas.TalkingIsland;

import org.l2jdd.gameserver.enums.ChatType;
import org.l2jdd.gameserver.model.actor.Npc;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.network.NpcStringId;

import ai.AbstractNpcAI;

/**
 * Oris AI.
 * @author Gladicek
 */
public class Oris extends AbstractNpcAI
{
	// NPCs
	private static final int ORIS = 33116;
	
	private Oris()
	{
		addSpawnId(ORIS);
		addStartNpc(ORIS);
		addTalkId(ORIS);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, PlayerInstance player)
	{
		if (event.equals("SOCIAL_ACTION_1"))
		{
			npc.broadcastSocialAction(6);
			startQuestTimer("SOCIAL_ACTION_2", 2500, npc, null);
		}
		else if (event.equals("SOCIAL_ACTION_2"))
		{
			npc.broadcastSocialAction(7);
		}
		else if (event.equals("SPAM_TEXT") && (npc != null))
		{
			npc.broadcastSay(ChatType.NPC_GENERAL, NpcStringId.I_HAVEN_T_FELT_THIS_GOOD_IN_AGES, 1000);
		}
		return super.onAdvEvent(event, npc, player);
	}
	
	@Override
	public String onSpawn(Npc npc)
	{
		npc.setRandomAnimation(false);
		startQuestTimer("SOCIAL_ACTION_1", 6500, npc, null, true);
		startQuestTimer("SPAM_TEXT", 10000, npc, null, true);
		return super.onSpawn(npc);
	}
	
	public static void main(String[] args)
	{
		new Oris();
	}
}