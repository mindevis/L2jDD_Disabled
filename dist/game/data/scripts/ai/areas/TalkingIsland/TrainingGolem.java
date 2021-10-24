
package ai.areas.TalkingIsland;

import org.l2jdd.gameserver.model.actor.Npc;

import ai.AbstractNpcAI;

/**
 * Training Golem AI.
 * @author Gladicek
 */
public class TrainingGolem extends AbstractNpcAI
{
	// NPCs
	private static final int TRAINING_GOLEM = 27532;
	
	private TrainingGolem()
	{
		addSpawnId(TRAINING_GOLEM);
	}
	
	@Override
	public String onSpawn(Npc npc)
	{
		npc.setImmobilized(true);
		return super.onSpawn(npc);
	}
	
	public static void main(String[] args)
	{
		new TrainingGolem();
	}
}
