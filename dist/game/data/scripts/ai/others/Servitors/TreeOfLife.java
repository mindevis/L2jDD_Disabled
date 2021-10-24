
package ai.others.Servitors;

import org.l2jdd.commons.util.CommonUtil;
import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.actor.Npc;
import org.l2jdd.gameserver.model.actor.Summon;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;

import ai.AbstractNpcAI;

/**
 * Tree of Life AI.
 * @author St3eT.
 */
public class TreeOfLife extends AbstractNpcAI
{
	// NPCs
	private static final int[] TREE_OF_LIFE =
	{
		14933,
		14943,
		15010,
		15011,
		15154,
	};
	
	private TreeOfLife()
	{
		addSummonSpawnId(TREE_OF_LIFE);
	}
	
	@Override
	public void onSummonSpawn(Summon summon)
	{
		getTimers().addTimer("HEAL", 3000, null, summon.getOwner());
	}
	
	@Override
	public void onTimerEvent(String event, StatSet params, Npc npc, PlayerInstance player)
	{
		if (player != null)
		{
			final Summon summon = player.getFirstServitor();
			if (event.equals("HEAL") && (summon != null) && CommonUtil.contains(TREE_OF_LIFE, summon.getId()))
			{
				summon.doCast(summon.getTemplate().getParameters().getSkillHolder("s_tree_heal").getSkill(), null, false, false);
				getTimers().addTimer("HEAL", 8000, null, player);
			}
		}
	}
	
	public static void main(String[] args)
	{
		new TreeOfLife();
	}
}