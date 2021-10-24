
package ai.others;

import org.l2jdd.gameserver.model.actor.Npc;

import ai.AbstractNpcAI;

/**
 * Scarecrow AI.
 * @author ivantotov
 */
public class Scarecrow extends AbstractNpcAI
{
	// NPCs
	private static final int TRAINING_DUMMY = 19546;
	private static final int SCARECROW = 27457;
	
	private Scarecrow()
	{
		addSpawnId(TRAINING_DUMMY, SCARECROW);
	}
	
	@Override
	public String onSpawn(Npc npc)
	{
		npc.disableCoreAI(true);
		npc.setImmobilized(true);
		return super.onSpawn(npc);
	}
	
	public static void main(String[] args)
	{
		new Scarecrow();
	}
}
