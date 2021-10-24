
package ai.bosses.Spezion;

import org.l2jdd.gameserver.model.actor.Npc;

import ai.AbstractNpcAI;

/**
 * Present Spezion AI.
 * @author St3eT
 */
public class PresentSpezion extends AbstractNpcAI
{
	// NPCs
	private static final int PRESENT_SPEZION = 32948;
	
	private PresentSpezion()
	{
		addSpawnId(PRESENT_SPEZION);
	}
	
	@Override
	public String onSpawn(Npc npc)
	{
		npc.setDisplayEffect(2);
		return super.onSpawn(npc);
	}
	
	public static void main(String[] args)
	{
		new PresentSpezion();
	}
}