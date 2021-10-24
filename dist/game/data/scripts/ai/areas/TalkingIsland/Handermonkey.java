
package ai.areas.TalkingIsland;

import org.l2jdd.gameserver.geoengine.GeoEngine;
import org.l2jdd.gameserver.model.Location;
import org.l2jdd.gameserver.model.actor.Npc;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;

import ai.AbstractNpcAI;

/**
 * Handermonkey AI.
 * @author Gladicek
 */
public class Handermonkey extends AbstractNpcAI
{
	// NPC
	private static final int HANDERMONKEY = 33203;
	
	private Handermonkey()
	{
		addSpawnId(HANDERMONKEY);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, PlayerInstance player)
	{
		if (event.equals("NPC_MOVE") && (npc != null))
		{
			if (getRandom(100) < 70)
			{
				final int x = npc.getSpawn().getX() + (getRandom(-100, 100));
				final int y = npc.getSpawn().getY() + (getRandom(-100, 100));
				final Location loc = GeoEngine.getInstance().getValidLocation(npc.getX(), npc.getY(), npc.getZ(), x, y, npc.getZ(), npc.getInstanceWorld());
				addMoveToDesire(npc, loc, 0);
			}
			else
			{
				npc.broadcastSocialAction(9);
			}
		}
		return super.onAdvEvent(event, npc, player);
	}
	
	@Override
	public String onSpawn(Npc npc)
	{
		npc.setRunning();
		cancelQuestTimer("NPC_MOVE", npc, null);
		startQuestTimer("NPC_MOVE", 5000, npc, null, true);
		return super.onSpawn(npc);
	}
	
	public static void main(String[] args)
	{
		new Handermonkey();
	}
}