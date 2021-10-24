
package ai.areas.DenOfEvil;

import org.l2jdd.gameserver.model.actor.Npc;

import ai.AbstractNpcAI;

/**
 * Ragna Orc Seer AI.
 * @author Zealar
 */
public class RagnaOrcSeer extends AbstractNpcAI
{
	private static final int RAGNA_ORC_SEER = 22697;
	
	private RagnaOrcSeer()
	{
		addSpawnId(RAGNA_ORC_SEER);
	}
	
	@Override
	public String onSpawn(Npc npc)
	{
		spawnMinions(npc, "Privates" + getRandom(1, 2));
		return super.onSpawn(npc);
	}
	
	public static void main(String[] args)
	{
		new RagnaOrcSeer();
	}
}
