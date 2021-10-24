
package ai.areas.FaeronVillage.RemembranceTower;

import org.l2jdd.gameserver.model.actor.Npc;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.network.serverpackets.OnEventTrigger;

import ai.AbstractNpcAI;

/**
 * Remembrance Tower AI.
 * @author St3eT
 */
public class RemembranceTower extends AbstractNpcAI
{
	// NPCs
	private static final int REMEMBRANCE_TOWER = 33989;
	// Misc
	private static final int EMMITER_ID = 17250700;
	
	private RemembranceTower()
	{
		addStartNpc(REMEMBRANCE_TOWER);
		addTalkId(REMEMBRANCE_TOWER);
		addFirstTalkId(REMEMBRANCE_TOWER);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, PlayerInstance player)
	{
		if (event.equals("action") && npc.isScriptValue(0))
		{
			npc.broadcastPacket(new OnEventTrigger(EMMITER_ID, true));
			npc.setScriptValue(1);
			startQuestTimer("TRIGGER", 3000, npc, null);
		}
		else if (event.equals("TRIGGER"))
		{
			npc.setScriptValue(0);
			npc.broadcastPacket(new OnEventTrigger(EMMITER_ID, false));
		}
		return super.onAdvEvent(event, npc, player);
	}
	
	public static void main(String[] args)
	{
		new RemembranceTower();
	}
}