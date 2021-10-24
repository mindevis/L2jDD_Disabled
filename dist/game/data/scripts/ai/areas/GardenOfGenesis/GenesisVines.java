
package ai.areas.GardenOfGenesis;

import org.l2jdd.gameserver.model.actor.Npc;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.model.holders.SkillHolder;
import org.l2jdd.gameserver.model.skills.SkillCaster;

import ai.AbstractNpcAI;

/**
 * Genesis Vines AI.
 * @author St3eT
 */
public class GenesisVines extends AbstractNpcAI
{
	// NPCs
	private static final int VINE = 18987; // Vine
	private static final int ROSE_VINE = 18988; // Rose Vine
	// Skills
	private static final SkillHolder VINE_SKILL = new SkillHolder(14092, 1);
	private static final SkillHolder ROSE_VINE_SKILL = new SkillHolder(14091, 1);
	
	private GenesisVines()
	{
		addSpawnId(VINE, ROSE_VINE);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, PlayerInstance player)
	{
		if (event.equals("CAST_SKILL") && npc.isScriptValue(1))
		{
			final SkillHolder skill = npc.getId() == VINE ? VINE_SKILL : ROSE_VINE_SKILL;
			if (SkillCaster.checkUseConditions(npc, skill.getSkill()))
			{
				addSkillCastDesire(npc, npc, skill, 23);
			}
			startQuestTimer("CAST_SKILL", 3000, npc, null);
		}
		else if (event.equals("DELETE"))
		{
			npc.setScriptValue(0);
			npc.deleteMe();
		}
		return super.onAdvEvent(event, npc, player);
	}
	
	@Override
	public String onSpawn(Npc npc)
	{
		npc.disableCoreAI(true);
		npc.setScriptValue(1);
		cancelQuestTimer("CAST_SKILL", npc, null);
		cancelQuestTimer("DELETE", npc, null);
		startQuestTimer("CAST_SKILL", 3000, npc, null);
		startQuestTimer("DELETE", 150000, npc, null);
		return super.onSpawn(npc);
	}
	
	public static void main(String[] args)
	{
		new GenesisVines();
	}
}