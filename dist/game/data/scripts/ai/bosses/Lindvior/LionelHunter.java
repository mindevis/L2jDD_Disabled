
package ai.bosses.Lindvior;

import org.l2jdd.gameserver.enums.ChatType;
import org.l2jdd.gameserver.instancemanager.WalkingManager;
import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.actor.Npc;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.network.NpcStringId;

import ai.AbstractNpcAI;

/**
 * Lionel Hunter AI
 * @author Gigi
 * @date 2017-07-23 - [22:54:59]
 */
public class LionelHunter extends AbstractNpcAI
{
	// Npc
	private static final int LIONEL_HUNTER = 33886;
	// Misc
	private static final String ROUTE_NAME = "Rune_Lionel";
	
	public LionelHunter()
	{
		addSpawnId(LIONEL_HUNTER);
	}
	
	@Override
	public void onTimerEvent(String event, StatSet params, Npc npc, PlayerInstance player)
	{
		if (event.equals("NPC_SHOUT") && (npc != null))
		{
			npc.broadcastSay(ChatType.NPC_GENERAL, NpcStringId.WE_JUST_LOCATED_LINDVIOR_THOSE_WHO_ARE_WILLING_TO_FIGHT_CAN_DO_SO_AT_ANY_TIME_NOW);
			getTimers().addTimer("NPC_SHOUT", (10 + getRandom(5)) * 1000, npc, null);
		}
	}
	
	@Override
	public String onSpawn(Npc npc)
	{
		getTimers().addTimer("NPC_SHOUT", (10 + getRandom(5)) * 1000, npc, null);
		WalkingManager.getInstance().startMoving(npc, ROUTE_NAME);
		return super.onSpawn(npc);
	}
	
	public static void main(String[] args)
	{
		new LionelHunter();
	}
}