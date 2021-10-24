
package ai.areas.WharfGludioAirships.ZealotOfShilen;

import org.l2jdd.gameserver.ai.CtrlIntention;
import org.l2jdd.gameserver.model.World;
import org.l2jdd.gameserver.model.actor.Attackable;
import org.l2jdd.gameserver.model.actor.Npc;
import org.l2jdd.gameserver.model.actor.instance.MonsterInstance;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;

import ai.AbstractNpcAI;

/**
 * Zealot of Shilen AI.
 * @author nonom, Mobius
 */
public class ZealotOfShilen extends AbstractNpcAI
{
	// NPCs
	private static final int ZEALOT = 18782;
	private static final int[] GUARDS =
	{
		32628,
		32629
	};
	
	public ZealotOfShilen()
	{
		addSpawnId(ZEALOT);
		addSpawnId(GUARDS);
		addFirstTalkId(GUARDS);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, PlayerInstance player)
	{
		if (npc == null)
		{
			return null;
		}
		
		if (!npc.isAttackingNow() && !npc.isAlikeDead())
		{
			Npc nearby = null;
			double maxDistance = Double.MAX_VALUE;
			for (MonsterInstance obj : World.getInstance().getVisibleObjects(npc, MonsterInstance.class))
			{
				final double distance = npc.calculateDistance2D(obj);
				if ((distance < maxDistance) && !obj.isDead() && !obj.isDecayed())
				{
					maxDistance = distance;
					nearby = obj;
				}
			}
			if (nearby != null)
			{
				npc.setRunning();
				((Attackable) npc).addDamageHate(nearby, 0, 999);
				npc.getAI().setIntention(CtrlIntention.AI_INTENTION_ATTACK, nearby, null);
			}
		}
		
		return super.onAdvEvent(event, npc, player);
	}
	
	@Override
	public String onFirstTalk(Npc npc, PlayerInstance player)
	{
		return (npc.isAttackingNow()) ? "32628-01.html" : npc.getId() + ".html";
	}
	
	@Override
	public String onSpawn(Npc npc)
	{
		if (npc.getId() == ZEALOT)
		{
			npc.setRandomWalking(false);
		}
		else
		{
			npc.setInvul(true);
			((Attackable) npc).setCanReturnToSpawnPoint(false);
			cancelQuestTimer("WATCHING", npc, null);
			startQuestTimer("WATCHING", 10000, npc, null, true);
		}
		return super.onSpawn(npc);
	}
	
	public static void main(String[] args)
	{
		new ZealotOfShilen();
	}
}
