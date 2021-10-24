
package ai.others;

import org.l2jdd.gameserver.model.actor.Npc;

import ai.AbstractNpcAI;

/**
 * @author UnAfraid
 */
public class NonLethalableNpcs extends AbstractNpcAI
{
	private static final int[] NPCS =
	{
		35062, // Headquarters
	};
	
	public NonLethalableNpcs()
	{
		addSpawnId(NPCS);
	}
	
	@Override
	public String onSpawn(Npc npc)
	{
		npc.setLethalable(false);
		return super.onSpawn(npc);
	}
	
	public static void main(String[] args)
	{
		new NonLethalableNpcs();
	}
}
