
package ai.others;

import org.l2jdd.Config;
import org.l2jdd.gameserver.model.WorldObject;
import org.l2jdd.gameserver.model.actor.Npc;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.model.holders.SkillHolder;
import org.l2jdd.gameserver.model.skills.Skill;
import org.l2jdd.gameserver.network.SystemMessageId;

import ai.AbstractNpcAI;

/**
 * Ballista AI.
 * @author St3eT
 */
public class Ballista extends AbstractNpcAI
{
	// NPCs
	private static final int[] BALLISTA =
	{
		35685, // Shanty Fortress
		35723, // Southern Fortress
		35754, // Hive Fortress
		35792, // Valley Fortress
		35823, // Ivory Fortress
		35854, // Narsell Fortress
		35892, // Bayou Fortress
		35923, // White Sands Fortress
		35961, // Borderland Fortress
		35999, // Swamp Fortress
		36030, // Archaic Fortress
		36068, // Floran Fortress
		36106, // Cloud Mountain)
		36137, // Tanor Fortress
		36168, // Dragonspine Fortress
		36206, // Antharas's Fortress
		36244, // Western Fortress
		36282, // Hunter's Fortress
		36313, // Aaru Fortress
		36351, // Demon Fortress
		36389, // Monastic Fortress
	};
	// Skill
	private static final SkillHolder BOMB = new SkillHolder(2342, 1); // Ballista Bomb
	// Misc
	private static final int MIN_CLAN_LV = 5;
	
	private Ballista()
	{
		addSkillSeeId(BALLISTA);
		addSpawnId(BALLISTA);
	}
	
	@Override
	public String onSkillSee(Npc npc, PlayerInstance caster, Skill skill, WorldObject[] targets, boolean isSummon)
	{
		if ((skill != null) && (caster.getTarget() == npc) && (getRandom(100) < 40) && (skill == BOMB.getSkill()))
		{
			if (npc.getFort().getSiege().isInProgress() && (caster.getClan() != null) && (caster.getClan().getLevel() >= MIN_CLAN_LV))
			{
				caster.getClan().addReputationScore(Config.BALLISTA_POINTS, true);
				caster.sendPacket(SystemMessageId.THE_BALLISTA_HAS_BEEN_SUCCESSFULLY_DESTROYED_THE_CLAN_REPUTATION_WILL_BE_INCREASED);
			}
			npc.doDie(caster);
		}
		return super.onSkillSee(npc, caster, skill, targets, isSummon);
	}
	
	@Override
	public String onSpawn(Npc npc)
	{
		npc.disableCoreAI(true);
		npc.setUndying(true);
		return super.onSpawn(npc);
	}
	
	public static void main(String[] args)
	{
		new Ballista();
	}
}
