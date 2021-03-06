
package ai.areas.TalkingIsland;

import org.l2jdd.gameserver.enums.ChatType;
import org.l2jdd.gameserver.model.actor.Npc;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.network.NpcStringId;

import ai.AbstractNpcAI;

/**
 * Heymond AI.
 * @author St3eT
 */
public class Heymond extends AbstractNpcAI
{
	// NPCs
	private static final int BANETTE = 33114;
	
	private Heymond()
	{
		addSpawnId(BANETTE);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, PlayerInstance player)
	{
		if (event.equals("NPC_SHOUT"))
		{
			switch (getRandom(4))
			{
				case 0:
				{
					npc.broadcastSay(ChatType.NPC_GENERAL, NpcStringId.VIEW_OUR_WIDE_VARIETY_OF_ACCESSORIES);
					break;
				}
				case 1:
				{
					npc.broadcastSay(ChatType.NPC_GENERAL, NpcStringId.THE_BEST_WEAPON_DOESN_T_MAKE_YOU_THE_BEST);
					break;
				}
				case 2:
				{
					npc.broadcastSay(ChatType.NPC_GENERAL, NpcStringId.WE_BUY_AND_SELL_COME_TAKE_A_LOOK);
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
		startQuestTimer("NPC_SHOUT", (10 + getRandom(5)) * 1000, npc, null);
		return super.onSpawn(npc);
	}
	
	public static void main(String[] args)
	{
		new Heymond();
	}
}