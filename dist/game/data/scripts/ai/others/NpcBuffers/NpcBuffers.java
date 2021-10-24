
package ai.others.NpcBuffers;

import org.l2jdd.commons.concurrent.ThreadPool;
import org.l2jdd.gameserver.model.actor.Npc;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;

import ai.AbstractNpcAI;

/**
 * @author UnAfraid
 */
public class NpcBuffers extends AbstractNpcAI
{
	private final NpcBuffersData _npcBuffers = new NpcBuffersData();
	
	private NpcBuffers()
	{
		for (int npcId : _npcBuffers.getNpcBufferIds())
		{
			// TODO: Cleanup once npc rework is finished and default html is configurable.
			addFirstTalkId(npcId);
			addSpawnId(npcId);
		}
	}
	
	// TODO: Cleanup once npc rework is finished and default html is configurable.
	@Override
	public String onFirstTalk(Npc npc, PlayerInstance player)
	{
		return null;
	}
	
	@Override
	public String onSpawn(Npc npc)
	{
		final NpcBufferData data = _npcBuffers.getNpcBuffer(npc.getId());
		for (NpcBufferSkillData skill : data.getSkills())
		{
			ThreadPool.schedule(new NpcBufferAI(npc, skill), skill.getInitialDelay());
		}
		return super.onSpawn(npc);
	}
	
	public static void main(String[] args)
	{
		new NpcBuffers();
	}
}
