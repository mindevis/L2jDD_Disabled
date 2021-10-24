
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
 * Boy and Girl AI.
 * @author St3eT
 */
public class BoyAndGirl extends AbstractNpcAI
{
	// NPCs
	private static final int BOY = 33224;
	private static final int GIRL = 33217;
	// Items
	private static final int WEAPON = 15304;
	
	private BoyAndGirl()
	{
		addSpawnId(BOY, GIRL);
		addMoveFinishedId(BOY, GIRL);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, PlayerInstance player)
	{
		if (event.equals("NPC_CHANGEWEAP"))
		{
			if (npc.isScriptValue(1))
			{
				npc.setRHandId(0);
				npc.setScriptValue(0);
			}
			else
			{
				npc.setRHandId(WEAPON);
				npc.setScriptValue(1);
			}
			startQuestTimer("NPC_CHANGEWEAP", 15000 + (getRandom(5) * 1000), npc, null);
		}
		else if (event.equals("NPC_SHOUT"))
		{
			npc.broadcastSay(ChatType.NPC_GENERAL, npc.getId() == BOY ? NpcStringId.WOW_2 : NpcStringId.BOYS_ARE_SO_ANNOYING);
			startQuestTimer("NPC_SHOUT", 10000 + (getRandom(5) * 1000), npc, null);
		}
		return super.onAdvEvent(event, npc, player);
	}
	
	@Override
	public String onSpawn(Npc npc)
	{
		cancelQuestTimer("NPC_CHANGEWEAP", npc, null);
		startQuestTimer("NPC_CHANGEWEAP", 15000 + (getRandom(5) * 1000), npc, null);
		cancelQuestTimer("NPC_SHOUT", npc, null);
		startQuestTimer("NPC_SHOUT", 10000 + (getRandom(5) * 1000), npc, null);
		npc.setRunning();
		final Location randomLoc = Util.getRandomPosition(npc.getSpawn().getLocation(), 200, 600);
		addMoveToDesire(npc, GeoEngine.getInstance().getValidLocation(npc.getLocation().getX(), npc.getLocation().getY(), npc.getLocation().getZ(), randomLoc.getX(), randomLoc.getY(), randomLoc.getZ(), npc.getInstanceWorld()), 23);
		return super.onSpawn(npc);
	}
	
	@Override
	public void onMoveFinished(Npc npc)
	{
		final Location randomLoc = Util.getRandomPosition(npc.getSpawn().getLocation(), 200, 600);
		addMoveToDesire(npc, GeoEngine.getInstance().getValidLocation(npc.getLocation().getX(), npc.getLocation().getY(), npc.getLocation().getZ(), randomLoc.getX(), randomLoc.getY(), randomLoc.getZ(), npc.getInstanceWorld()), 23);
		super.onMoveFinished(npc);
	}
	
	public static void main(String[] args)
	{
		new BoyAndGirl();
	}
}