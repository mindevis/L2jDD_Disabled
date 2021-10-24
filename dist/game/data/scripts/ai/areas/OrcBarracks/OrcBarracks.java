
package ai.areas.OrcBarracks;

import org.l2jdd.gameserver.enums.Race;
import org.l2jdd.gameserver.model.actor.Npc;
import org.l2jdd.gameserver.model.actor.instance.MonsterInstance;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.network.NpcStringId;
import org.l2jdd.gameserver.network.serverpackets.ExShowScreenMessage;

import ai.AbstractNpcAI;

/**
 * Orc Barracks AI
 * @author malyelfik
 */
public class OrcBarracks extends AbstractNpcAI
{
	// NPC
	private static final int TUREK_ORC_FOOTMAN = 20499;
	private static final int TUREK_WAR_HOUND = 20494;
	private static final int CHERTUBA_MIRAGE = 23421;
	private static final int CHERTUBA_ILLUSION = 23422;
	private static final int[] MOBS =
	{
		20495, // Turek Orc Prefect
		20496, // Turek Orc Archer
		20497, // Turek Orc Skirmisher
		20498, // Turek Orc Supplier
		20500, // Turek Orc Sentinel
		20501, // Turek Orc Priest
		20546, // Turek Orc Elder
		23418, // Marionette Spirit
	};
	// Misc
	private static final int MINION_COUNT = 2;
	private static final int SPAWN_RATE = 80;
	
	public OrcBarracks()
	{
		addSpawnId(TUREK_ORC_FOOTMAN);
		addKillId(TUREK_ORC_FOOTMAN);
		addKillId(MOBS);
	}
	
	@Override
	public String onSpawn(Npc npc)
	{
		final boolean hasMinions = npc.getParameters().getBoolean("hasMinions", false);
		if (hasMinions)
		{
			for (int i = 0; i < MINION_COUNT; i++)
			{
				addMinion((MonsterInstance) npc, TUREK_WAR_HOUND);
			}
		}
		return super.onSpawn(npc);
	}
	
	@Override
	public String onKill(Npc npc, PlayerInstance killer, boolean isSummon)
	{
		if ((killer.getRace() == Race.ERTHEIA) && (SPAWN_RATE > getRandom(100)))
		{
			final int npcId = (killer.isMageClass()) ? CHERTUBA_ILLUSION : CHERTUBA_MIRAGE;
			showOnScreenMsg(killer, NpcStringId.A_POWERFUL_MONSTER_HAS_COME_TO_FACE_YOU, ExShowScreenMessage.TOP_CENTER, 5000);
			addSpawn(npcId, npc, false, 180000);
		}
		return super.onKill(npc, killer, isSummon);
	}
	
	public static void main(String[] args)
	{
		new OrcBarracks();
	}
}
