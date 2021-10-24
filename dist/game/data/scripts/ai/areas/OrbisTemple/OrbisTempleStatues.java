
package ai.areas.OrbisTemple;

import org.l2jdd.gameserver.model.actor.Npc;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;

import ai.AbstractNpcAI;

/**
 * Orbis Temple Statues AI.
 * @author Mobius
 */
public class OrbisTempleStatues extends AbstractNpcAI
{
	// Npcs
	private static final int VICTIM_1 = 22911;
	private static final int VICTIM_2 = 22912;
	private static final int VICTIM_3 = 22913;
	private static final int GUARD_1 = 22914;
	private static final int GUARD_2 = 22915;
	private static final int GUARD_3 = 22916;
	private static final int THROWER_1 = 22917;
	private static final int THROWER_2 = 22918;
	private static final int THROWER_3 = 22919;
	private static final int ANCIENT_HERO = 22925;
	private static final int CURATOR = 22923;
	private static final int CHIEF_CURATOR = 22927;
	// Items
	private static final int SWORD = 15280;
	private static final int SPEAR = 17372;
	
	public OrbisTempleStatues()
	{
		addSpawnId(VICTIM_1, VICTIM_2, VICTIM_3, GUARD_1, GUARD_2, GUARD_3, THROWER_1, THROWER_2, THROWER_3, CURATOR, ANCIENT_HERO, CHIEF_CURATOR);
		addAttackId(VICTIM_1, VICTIM_2, VICTIM_3, GUARD_1, GUARD_2, GUARD_3, THROWER_1, THROWER_2, THROWER_3, CURATOR);
	}
	
	@Override
	public String onAttack(Npc npc, PlayerInstance attacker, int damage, boolean isPet)
	{
		switch (npc.getId())
		{
			case VICTIM_1:
			case VICTIM_2:
			case VICTIM_3:
			case GUARD_1:
			case GUARD_2:
			case GUARD_3:
			case CURATOR:
			{
				if (npc.isImmobilized())
				{
					npc.setImmobilized(false);
					npc.setRHandId(SWORD);
				}
				break;
			}
			case THROWER_1:
			case THROWER_2:
			case THROWER_3:
			{
				if (npc.isImmobilized())
				{
					npc.setImmobilized(false);
					npc.setRHandId(SPEAR);
				}
				break;
			}
		}
		return super.onAttack(npc, attacker, damage, isPet);
	}
	
	@Override
	public String onSpawn(Npc npc)
	{
		npc.setRandomWalking(false);
		if (npc.getId() < ANCIENT_HERO)
		{
			npc.setImmobilized(true);
		}
		return super.onSpawn(npc);
	}
	
	public static void main(String[] args)
	{
		new OrbisTempleStatues();
	}
}