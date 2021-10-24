
package ai.areas.DenOfEvil;

import org.l2jdd.gameserver.model.actor.Npc;

import ai.AbstractNpcAI;

/**
 * Ragna Orc Commander AI.
 * @author Zealar
 */
public class RagnaOrcCommander extends AbstractNpcAI
{
	private static final int RAGNA_ORC_COMMANDER = 22694;
	
	private RagnaOrcCommander()
	{
		addSpawnId(RAGNA_ORC_COMMANDER);
	}
	
	@Override
	public String onSpawn(Npc npc)
	{
		spawnMinions(npc, "Privates1");
		spawnMinions(npc, getRandomBoolean() ? "Privates2" : "Privates3");
		return super.onSpawn(npc);
	}
	
	public static void main(String[] args)
	{
		new RagnaOrcCommander();
	}
}
