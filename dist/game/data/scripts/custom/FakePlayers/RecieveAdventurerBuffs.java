
package custom.FakePlayers;

import org.l2jdd.Config;
import org.l2jdd.commons.util.CommonUtil;
import org.l2jdd.gameserver.data.xml.FakePlayerData;
import org.l2jdd.gameserver.enums.ClassId;
import org.l2jdd.gameserver.model.World;
import org.l2jdd.gameserver.model.actor.Npc;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.model.holders.SkillHolder;
import org.l2jdd.gameserver.model.skills.SkillCaster;

import ai.AbstractNpcAI;

/**
 * Town Fake Player walkers that receive buffs from Adventurer NPC.
 * @author Mobius
 */
public class RecieveAdventurerBuffs extends AbstractNpcAI
{
	// NPCs
	private static final int[] ADVENTURERS_GUIDE =
	{
		32327,
		33950,
	};
	private static final int[] FAKE_PLAYER_IDS =
	{
		80000
	};
	// Skills
	private static final SkillHolder FANTASIA = new SkillHolder(32840, 1); // Fantasia Harmony - Adventurer
	private static final SkillHolder[] GROUP_BUFFS =
	{
		new SkillHolder(15642, 1), // Horn Melody (Adventurer)
		new SkillHolder(15643, 1), // Drum Melody (Adventurer)
		new SkillHolder(15644, 1), // Pipe Organ Melody (Adventurer)
		new SkillHolder(15645, 1), // Guitar Melody (Adventurer)
		new SkillHolder(15651, 1), // Prevailing Sonata (Adventurer)
		new SkillHolder(15652, 1), // Daring Sonata (Adventurer)
		new SkillHolder(15653, 1), // Refreshing Sonata (Adventurer)
	};
	
	private RecieveAdventurerBuffs()
	{
		if (Config.FAKE_PLAYERS_ENABLED)
		{
			addSpawnId(FAKE_PLAYER_IDS);
		}
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, PlayerInstance player)
	{
		if (event.startsWith("AUTOBUFF") && (npc != null) && !npc.isDead())
		{
			if (!npc.isMoving())
			{
				for (Npc nearby : World.getInstance().getVisibleObjectsInRange(npc, Npc.class, 100))
				{
					if (CommonUtil.contains(ADVENTURERS_GUIDE, nearby.getId()))
					{
						for (SkillHolder holder : GROUP_BUFFS)
						{
							SkillCaster.triggerCast(nearby, npc, holder.getSkill());
						}
						if (ClassId.getClassId(FakePlayerData.getInstance().getInfo(npc.getId()).getClassId()).isMage())
						{
							SkillCaster.triggerCast(nearby, npc, FANTASIA.getSkill()); // TODO: Merge events.
						}
						else
						{
							SkillCaster.triggerCast(nearby, npc, FANTASIA.getSkill()); // TODO: Merge events.
						}
						break;
					}
				}
			}
			startQuestTimer("AUTOBUFF", 30000, npc, null);
		}
		return super.onAdvEvent(event, npc, player);
	}
	
	@Override
	public String onSpawn(Npc npc)
	{
		startQuestTimer("AUTOBUFF", 1000, npc, null);
		return super.onSpawn(npc);
	}
	
	public static void main(String[] args)
	{
		new RecieveAdventurerBuffs();
	}
}
