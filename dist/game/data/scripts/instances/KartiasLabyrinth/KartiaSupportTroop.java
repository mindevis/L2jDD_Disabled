
package instances.KartiasLabyrinth;

import java.util.List;

import org.l2jdd.gameserver.enums.ChatType;
import org.l2jdd.gameserver.geoengine.GeoEngine;
import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.World;
import org.l2jdd.gameserver.model.actor.Npc;
import org.l2jdd.gameserver.model.actor.instance.MonsterInstance;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.network.NpcStringId;

import ai.AbstractNpcAI;

/**
 * Kartia Support Troop AI.
 * @author St3eT
 */
public class KartiaSupportTroop extends AbstractNpcAI
{
	// NPCs
	private static final int[] SUPPORT_TROOPS =
	{
		33642, // Support Troop (Kartia 85)
		33644, // Support Troop (Kartia 90)
		33646, // Support Troop (Kartia 95)
	};
	
	private KartiaSupportTroop()
	{
		addSpawnId(SUPPORT_TROOPS);
	}
	
	@Override
	public void onTimerEvent(String event, StatSet params, Npc npc, PlayerInstance player)
	{
		if (event.equals("NPC_SAY") && !npc.isDead())
		{
			npc.broadcastSay(ChatType.NPC_GENERAL, NpcStringId.DEFEAT_ALL_THE_MONSTERS);
			getTimers().addTimer("NPC_SAY", 20000, npc, null);
		}
		else if (event.equals("CHECK_TARGET") && (!npc.isInCombat() || !npc.isAttackingNow() || (npc.getTarget() == null)))
		{
			final List<MonsterInstance> monsterList = World.getInstance().getVisibleObjects(npc, MonsterInstance.class);
			if (!monsterList.isEmpty())
			{
				final MonsterInstance monster = monsterList.get(getRandom(monsterList.size()));
				if (monster.isTargetable() && GeoEngine.getInstance().canSeeTarget(npc, monster))
				{
					addAttackDesire(npc, monster);
				}
			}
		}
	}
	
	@Override
	public String onSpawn(Npc npc)
	{
		if (npc.getInstanceWorld() != null)
		{
			npc.broadcastSay(ChatType.NPC_GENERAL, NpcStringId.VANGUARD_OF_ADEN_WE_HAVE_RETURNED);
			getTimers().addTimer("NPC_SAY", 20000, npc, null);
			getTimers().addRepeatingTimer("CHECK_TARGET", 1000, npc, null);
		}
		return super.onSpawn(npc);
	}
	
	public static void main(String[] args)
	{
		new KartiaSupportTroop();
	}
}