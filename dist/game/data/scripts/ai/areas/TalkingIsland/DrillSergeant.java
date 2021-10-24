
package ai.areas.TalkingIsland;

import org.l2jdd.gameserver.model.World;
import org.l2jdd.gameserver.model.actor.Npc;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;

import ai.AbstractNpcAI;

/**
 * Drill Sergeant AI.
 * @author St3eT
 */
public class DrillSergeant extends AbstractNpcAI
{
	// NPCs
	private static final int SERGANT = 33007; // Drill Sergant
	private static final int GUARD = 33018;
	// Misc
	//@formatter:off
	private final int[] SOCIAL_ACTIONS = {9, 10, 11, 1 };
	//@formatter:on
	
	private DrillSergeant()
	{
		addSpawnId(SERGANT);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, PlayerInstance player)
	{
		if (event.equals("SOCIAL_SHOW"))
		{
			final int socialActionId = getRandomEntry(SOCIAL_ACTIONS);
			npc.broadcastSocialAction(socialActionId);
			
			World.getInstance().forEachVisibleObjectInRange(npc, Npc.class, 500, chars ->
			{
				if (chars.getId() == GUARD)
				{
					chars.getVariables().set("SOCIAL_ACTION_ID", socialActionId);
					startQuestTimer("SOCIAL_ACTION", getRandom(500, 1500), chars, null);
				}
			});
		}
		else if (event.equals("SOCIAL_ACTION"))
		{
			final int socialActionId = npc.getVariables().getInt("SOCIAL_ACTION_ID", 0);
			if (socialActionId > 0)
			{
				npc.broadcastSocialAction(socialActionId);
			}
		}
		return super.onAdvEvent(event, npc, player);
	}
	
	@Override
	public String onSpawn(Npc npc)
	{
		if (npc.getId() == SERGANT)
		{
			startQuestTimer("SOCIAL_SHOW", 10000, npc, null, true);
		}
		npc.setRandomAnimation(false);
		return super.onSpawn(npc);
	}
	
	public static void main(String[] args)
	{
		new DrillSergeant();
	}
}