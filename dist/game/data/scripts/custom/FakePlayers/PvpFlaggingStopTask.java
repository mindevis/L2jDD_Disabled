
package custom.FakePlayers;

import org.l2jdd.Config;
import org.l2jdd.gameserver.model.WorldObject;
import org.l2jdd.gameserver.model.actor.Npc;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;

import ai.AbstractNpcAI;

/**
 * TODO: Move it to Creature.
 * @author Mobius
 */
public class PvpFlaggingStopTask extends AbstractNpcAI
{
	private PvpFlaggingStopTask()
	{
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, PlayerInstance player)
	{
		if (npc == null)
		{
			return null;
		}
		if (npc.isDead())
		{
			cancelQuestTimer("FLAG_CHECK", npc, null);
			cancelQuestTimer("FINISH_FLAG", npc, null);
			cancelQuestTimer("REMOVE_FLAG", npc, null);
			return null;
		}
		
		if (event.equals("FLAG_CHECK"))
		{
			final WorldObject target = npc.getTarget();
			if ((target != null) && (target.isPlayable() || target.isFakePlayer()))
			{
				npc.setScriptValue(1); // in combat
				cancelQuestTimer("FINISH_FLAG", npc, null);
				cancelQuestTimer("REMOVE_FLAG", npc, null);
				startQuestTimer("FINISH_FLAG", Config.PVP_NORMAL_TIME - 20000, npc, null);
				startQuestTimer("FLAG_CHECK", 5000, npc, null);
			}
		}
		else if (event.equals("FINISH_FLAG"))
		{
			if (npc.isScriptValue(1))
			{
				npc.setScriptValue(2); // blink status
				npc.broadcastInfo(); // update flag status
				startQuestTimer("REMOVE_FLAG", 20000, npc, null);
			}
		}
		else if (event.equals("REMOVE_FLAG"))
		{
			if (npc.isScriptValue(2))
			{
				npc.setScriptValue(0); // not in combat
				npc.broadcastInfo(); // update flag status
			}
		}
		return super.onAdvEvent(event, npc, player);
	}
	
	public static void main(String[] args)
	{
		new PvpFlaggingStopTask();
	}
}
