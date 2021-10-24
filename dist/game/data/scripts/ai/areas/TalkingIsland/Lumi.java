
package ai.areas.TalkingIsland;

import org.l2jdd.gameserver.enums.ChatType;
import org.l2jdd.gameserver.model.actor.Npc;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.network.NpcStringId;

import ai.AbstractNpcAI;

/**
 * Lumi AI.
 * @author Gladicek
 */
public class Lumi extends AbstractNpcAI
{
	// NPCs
	private static final int LUMI = 33025;
	// Misc
	private static final NpcStringId[] LUMI_SHOUT =
	{
		NpcStringId.TO_YOUR_RIGHT_THE_ADMINISTRATIVE_DISTRICT_AND_TO_THE_LEFT_IS_THE_MUSEUM,
		NpcStringId.WHEN_YOU_USE_THE_TELEPORTER_YOU_CAN_GO_TO_THE_RUINS_OF_YE_SAGIRA,
		NpcStringId.HAVE_YOU_BEEN_TO_RUINS_OF_YE_SAGIRA_YOU_HAVE_TO_GO_AT_LEAST_ONCE,
	};
	
	private Lumi()
	{
		addSpawnId(LUMI);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, PlayerInstance player)
	{
		if (event.equals("SPAM_TEXT") && (npc != null))
		{
			npc.broadcastSay(ChatType.NPC_GENERAL, LUMI_SHOUT[getRandom(3)], 1000);
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
		new Lumi();
	}
}