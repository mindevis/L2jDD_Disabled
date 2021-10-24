
package ai.others;

import org.l2jdd.gameserver.model.actor.Attackable;
import org.l2jdd.gameserver.model.actor.Creature;
import org.l2jdd.gameserver.model.actor.Npc;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;

import ai.AbstractNpcAI;

/**
 * Gordon AI
 * @author TOFIZ, malyelfik
 */
public class Gordon extends AbstractNpcAI
{
	private static final int GORDON = 29095;
	
	private Gordon()
	{
		addSpawnId(GORDON);
		addSeeCreatureId(GORDON);
	}
	
	@Override
	public String onSeeCreature(Npc npc, Creature creature, boolean isSummon)
	{
		if (creature.isPlayer() && ((PlayerInstance) creature).isCursedWeaponEquipped())
		{
			addAttackPlayerDesire(npc, (PlayerInstance) creature);
		}
		return super.onSeeCreature(npc, creature, isSummon);
	}
	
	@Override
	public String onSpawn(Npc npc)
	{
		((Attackable) npc).setCanReturnToSpawnPoint(false);
		return super.onSpawn(npc);
	}
	
	public static void main(String[] args)
	{
		new Gordon();
	}
}