
package ai.areas.TalkingIsland;

import org.l2jdd.gameserver.model.World;
import org.l2jdd.gameserver.model.actor.Npc;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;

import ai.AbstractNpcAI;

/**
 * Trainning Soldier AI.
 * @author Mobius
 */
public class TrainningSoldier extends AbstractNpcAI
{
	// NPCs
	private static final int SOLDIER = 33201; // Trainning Soldier
	private static final int DUMMY = 33023; // Trainning Dummy
	
	private TrainningSoldier()
	{
		addSpawnId(SOLDIER);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, PlayerInstance player)
	{
		if ((npc != null) && !npc.isDead())
		{
			if (!npc.isInCombat())
			{
				for (Npc nearby : World.getInstance().getVisibleObjectsInRange(npc, Npc.class, 150))
				{
					if ((nearby != null) && (nearby.getId() == DUMMY))
					{
						addAttackDesire(npc, nearby);
						break;
					}
				}
			}
			startQuestTimer("START_ATTACK", 10000, npc, null);
		}
		return super.onAdvEvent(event, npc, player);
	}
	
	@Override
	public String onSpawn(Npc npc)
	{
		npc.setRandomAnimation(false);
		startQuestTimer("START_ATTACK", 5000, npc, null);
		return super.onSpawn(npc);
	}
	
	public static void main(String[] args)
	{
		new TrainningSoldier();
	}
}