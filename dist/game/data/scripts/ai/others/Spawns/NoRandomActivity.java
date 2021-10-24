
package ai.others.Spawns;

import org.l2jdd.gameserver.model.actor.Npc;
import org.l2jdd.gameserver.model.spawns.SpawnGroup;
import org.l2jdd.gameserver.model.spawns.SpawnTemplate;

import ai.AbstractNpcAI;

/**
 * @author UnAfraid
 */
public class NoRandomActivity extends AbstractNpcAI
{
	private NoRandomActivity()
	{
	}
	
	@Override
	public void onSpawnNpc(SpawnTemplate template, SpawnGroup group, Npc npc)
	{
		npc.setRandomAnimation(npc.getParameters().getBoolean("disableRandomAnimation", false));
		npc.setRandomWalking(npc.getParameters().getBoolean("disableRandomWalk", false));
		if (npc.getSpawn() != null)
		{
			npc.getSpawn().setRandomWalking(!npc.getParameters().getBoolean("disableRandomWalk", false));
		}
	}
	
	public static void main(String[] args)
	{
		new NoRandomActivity();
	}
}
