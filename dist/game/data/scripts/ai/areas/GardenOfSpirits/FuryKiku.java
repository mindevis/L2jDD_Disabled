
package ai.areas.GardenOfSpirits;

import org.l2jdd.gameserver.ai.CtrlEvent;
import org.l2jdd.gameserver.model.Party;
import org.l2jdd.gameserver.model.World;
import org.l2jdd.gameserver.model.actor.Npc;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;

import ai.AbstractNpcAI;

/**
 * Fury Kiku AI
 * @author Gigi
 * @date 2018-07-23 - [15:47:15]
 */
public class FuryKiku extends AbstractNpcAI
{
	// Monsters
	private static final int FURYKIKU = 23545;
	private static final int[] MONSTERS =
	{
		23544, // Fury Sylph Barrena
		23553, // Fury Sylph Barrena (night)
	};
	
	public FuryKiku()
	{
		addKillId(MONSTERS);
		addSpawnId(FURYKIKU);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, PlayerInstance player)
	{
		switch (event)
		{
			case "SPAWN":
			{
				final Party party = player.getParty();
				if (party != null)
				{
					party.getMembers().forEach(p -> addSpawn(FURYKIKU, p, true, 180000, true, 0));
				}
				else
				{
					addSpawn(FURYKIKU, player, true, 180000, true, 0);
				}
				break;
			}
			case "ATTACK":
			{
				npc.setRunning();
				World.getInstance().forEachVisibleObjectInRange(npc, PlayerInstance.class, 300, p ->
				{
					if ((p != null) && p.isPlayable() && !p.isDead())
					{
						npc.getAI().notifyEvent(CtrlEvent.EVT_AGGRESSION, p, 1000);
					}
				});
				break;
			}
		}
		return super.onAdvEvent(event, npc, player);
	}
	
	@Override
	public String onKill(Npc npc, PlayerInstance killer, boolean isSummon)
	{
		if (getRandom(10) < 5)
		{
			startQuestTimer("SPAWN", 2000, npc, killer);
		}
		return super.onKill(npc, killer, isSummon);
	}
	
	@Override
	public String onSpawn(Npc npc)
	{
		startQuestTimer("ATTACK", 1000, npc, null);
		return super.onSpawn(npc);
	}
	
	public static void main(String[] args)
	{
		new FuryKiku();
	}
}