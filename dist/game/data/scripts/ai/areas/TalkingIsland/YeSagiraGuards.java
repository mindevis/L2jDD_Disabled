
package ai.areas.TalkingIsland;

import org.l2jdd.gameserver.model.World;
import org.l2jdd.gameserver.model.actor.Npc;
import org.l2jdd.gameserver.model.actor.instance.MonsterInstance;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;

import ai.AbstractNpcAI;

/**
 * Ye Sagira Guards AI.
 * @author Mobius
 */
public class YeSagiraGuards extends AbstractNpcAI
{
	// NPCs
	private static final int[] GUARDS =
	{
		19152,
		19153
	};
	
	private YeSagiraGuards()
	{
		addSpawnId(GUARDS);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, PlayerInstance player)
	{
		if ((npc != null) && !npc.isDead())
		{
			if (!npc.isInCombat())
			{
				final MonsterInstance monster = getRandomEntry(World.getInstance().getVisibleObjectsInRange(npc, MonsterInstance.class, 1000));
				if ((monster != null) && !monster.isDead() && !monster.isInCombat())
				{
					npc.reduceCurrentHp(1, monster, null); // TODO: Find better way for attack
				}
			}
			startQuestTimer("GUARD_AGGRO", 10000, npc, null);
		}
		return super.onAdvEvent(event, npc, player);
	}
	
	@Override
	public String onSpawn(Npc npc)
	{
		npc.setInvul(true);
		startQuestTimer("GUARD_AGGRO", 5000, npc, null);
		return super.onSpawn(npc);
	}
	
	public static void main(String[] args)
	{
		new YeSagiraGuards();
	}
}
