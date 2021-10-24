
package ai.areas.DenOfEvil;

import org.l2jdd.gameserver.model.actor.Npc;

import ai.AbstractNpcAI;

/**
 * Ragna Orc Hero AI.
 * @author Zealar
 */
public class RagnaOrcHero extends AbstractNpcAI
{
	private static final int RAGNA_ORC_HERO = 22693;
	
	private RagnaOrcHero()
	{
		addSpawnId(RAGNA_ORC_HERO);
	}
	
	@Override
	public String onSpawn(Npc npc)
	{
		spawnMinions(npc, getRandom(100) < 70 ? "Privates1" : "Privates2");
		return super.onSpawn(npc);
	}
	
	public static void main(String[] args)
	{
		new RagnaOrcHero();
	}
}
