
package ai.areas.TalkingIsland;

import org.l2jdd.gameserver.enums.ChatType;
import org.l2jdd.gameserver.geoengine.GeoEngine;
import org.l2jdd.gameserver.model.Location;
import org.l2jdd.gameserver.model.actor.Npc;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.network.NpcStringId;
import org.l2jdd.gameserver.util.Util;

import ai.AbstractNpcAI;

/**
 * Devno AI.
 * @author St3eT
 */
public class Devno extends AbstractNpcAI
{
	// NPC
	private static final int DEVNO = 33241;
	
	private Devno()
	{
		addSpawnId(DEVNO);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, PlayerInstance player)
	{
		if (event.equals("NPC_MOVE"))
		{
			if (getRandomBoolean())
			{
				final Location randomLoc = Util.getRandomPosition(npc.getSpawn().getLocation(), 0, 500);
				addMoveToDesire(npc, GeoEngine.getInstance().getValidLocation(npc.getLocation().getX(), npc.getLocation().getY(), npc.getLocation().getZ(), randomLoc.getX(), randomLoc.getY(), randomLoc.getZ(), npc.getInstanceWorld()), 23);
			}
			startQuestTimer("NPC_MOVE", (10 + getRandom(5)) * 1000, npc, null);
		}
		else if (event.equals("NPC_SHOUT"))
		{
			switch (getRandom(4))
			{
				case 0:
				{
					npc.broadcastSay(ChatType.NPC_GENERAL, NpcStringId.CARRY_OUT_YOUR_QUESTS_FAITHFULLY_IN_TALKING_ISLAND_AND_YOU_LL_GET_TO_THE_1ST_CLASS_TRANSFER_IN_NO_TIME);
					break;
				}
				case 1:
				{
					npc.broadcastSay(ChatType.NPC_GENERAL, NpcStringId.I_SEE_THAT_ADVENTURERS_ARE_RETURNING_TO_TALKING_ISLAND_FOR_THE_AWAKENING);
					break;
				}
				case 2:
				{
					npc.broadcastSay(ChatType.NPC_GENERAL, NpcStringId.YOU_CAN_SEE_VARIOUS_STATISTICS_IN_THE_MUSEUM_STATS_IN_THE_MAIN_MENU);
					break;
				}
			}
			startQuestTimer("NPC_SHOUT", (10 + getRandom(5)) * 1000, npc, null);
		}
		return super.onAdvEvent(event, npc, player);
	}
	
	@Override
	public String onSpawn(Npc npc)
	{
		startQuestTimer("NPC_MOVE", (10 + getRandom(5)) * 1000, npc, null);
		startQuestTimer("NPC_SHOUT", (10 + getRandom(5)) * 1000, npc, null);
		return super.onSpawn(npc);
	}
	
	public static void main(String[] args)
	{
		new Devno();
	}
}