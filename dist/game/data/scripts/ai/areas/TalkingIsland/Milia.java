
package ai.areas.TalkingIsland;

import org.l2jdd.gameserver.enums.ChatType;
import org.l2jdd.gameserver.model.Location;
import org.l2jdd.gameserver.model.actor.Npc;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.network.NpcStringId;

import ai.AbstractNpcAI;

/**
 * Milia AI.
 * @author St3eT
 */
public class Milia extends AbstractNpcAI
{
	// NPCs
	private static final int MILIA = 30006;
	// Locations
	private static final Location GLUDIO_AIRSHIP = new Location(-149406, 255247, -80);
	
	private Milia()
	{
		addSpawnId(MILIA);
		addStartNpc(MILIA);
		addTalkId(MILIA);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, PlayerInstance player)
	{
		if (event.equals("gludioAirship"))
		{
			player.teleToLocation(GLUDIO_AIRSHIP);
		}
		else if (event.equals("TEXT_SPAM") && (npc != null))
		{
			npc.broadcastSay(ChatType.NPC_GENERAL, NpcStringId.SPEAK_WITH_ME_ABOUT_TRAVELING_AROUND_ADEN, 1000);
		}
		return super.onAdvEvent(event, npc, player);
	}
	
	@Override
	public String onSpawn(Npc npc)
	{
		startQuestTimer("TEXT_SPAM", 10000, npc, null, true);
		return super.onSpawn(npc);
	}
	
	public static void main(String[] args)
	{
		new Milia();
	}
}