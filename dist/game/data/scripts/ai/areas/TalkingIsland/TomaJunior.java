
package ai.areas.TalkingIsland;

import org.l2jdd.gameserver.enums.ChatType;
import org.l2jdd.gameserver.model.actor.Npc;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.network.NpcStringId;

import ai.AbstractNpcAI;

/**
 * Guard Soldier AI.
 * @author Gladicek
 */
public class TomaJunior extends AbstractNpcAI
{
	// NPCs
	private static final int TOMA_JUNIOR = 33571;
	
	private TomaJunior()
	{
		addSpawnId(TOMA_JUNIOR);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, PlayerInstance player)
	{
		if (event.equals("SPAM_TEXT") && (npc != null))
		{
			npc.broadcastSocialAction(3);
			npc.broadcastSay(ChatType.NPC_GENERAL, NpcStringId.THE_BEACH_WHERE_RELICS_OF_GIANTS_HAD_FALLEN_HAS_NOW_BECOME_CLEAN, 1000);
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
		startQuestTimer("SPAM_TEXT", 9000, npc, null, true);
		startQuestTimer("SOCIAL_ACTION", 15000, npc, null, true);
		return super.onSpawn(npc);
	}
	
	public static void main(String[] args)
	{
		new TomaJunior();
	}
}