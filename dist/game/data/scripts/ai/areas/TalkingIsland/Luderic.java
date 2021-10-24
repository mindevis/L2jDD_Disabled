
package ai.areas.TalkingIsland;

import org.l2jdd.gameserver.enums.ChatType;
import org.l2jdd.gameserver.model.actor.Npc;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.network.NpcStringId;

import ai.AbstractNpcAI;

/**
 * Luderic AI.
 * @author Gladicek
 */
public class Luderic extends AbstractNpcAI
{
	// NPCs
	private static final int LUDERIC = 33575;
	// Misc
	private static final NpcStringId[] LUDERIC_SHOUT =
	{
		NpcStringId.THERE_IS_A_DAY_WHERE_YOU_CAN_SEE_EVEN_THE_ADEN_CONTINENT_IF_THE_WEATHER_IS_GOOD,
		NpcStringId.IF_I_M_HERE_IT_FEELS_LIKE_TIME_HAS_STOPPED
	};
	
	private Luderic()
	{
		addSpawnId(LUDERIC);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, PlayerInstance player)
	{
		if (event.equals("SPAM_TEXT") && (npc != null))
		{
			npc.broadcastSay(ChatType.NPC_GENERAL, LUDERIC_SHOUT[getRandom(2)], 1000);
		}
		else if (event.equals("SOCIAL_ACTION") && (npc != null))
		{
			npc.broadcastSocialAction(1);
		}
		return super.onAdvEvent(event, npc, player);
	}
	
	@Override
	public String onSpawn(Npc npc)
	{
		startQuestTimer("SPAM_TEXT", 7000, npc, null, true);
		startQuestTimer("SOCIAL_ACTION", 3000, npc, null, true);
		return super.onSpawn(npc);
	}
	
	public static void main(String[] args)
	{
		new Luderic();
	}
}