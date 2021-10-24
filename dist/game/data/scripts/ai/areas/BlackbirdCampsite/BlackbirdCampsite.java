
package ai.areas.BlackbirdCampsite;

import org.l2jdd.gameserver.model.actor.Npc;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;

import ai.AbstractNpcAI;

/**
 * Soul Summon Stone AI.
 * @author NviX
 */
public final class BlackbirdCampsite extends AbstractNpcAI
{
	// NPCs
	private static final int SOUL_SUMMON_STONE = 34434;
	private static final int VALLERIA = 34435;
	// Bosses
	private static final int SUMMONED_HARPAS = 26347;
	private static final int SUMMONED_GARP = 26348;
	private static final int SUMMONED_MORICKS = 26349;
	// Items
	private static final int SOUL_QUARTZ = 48536;
	// Misc
	private Npc BOSS;
	
	private BlackbirdCampsite()
	{
		addStartNpc(SOUL_SUMMON_STONE, VALLERIA);
		addFirstTalkId(SOUL_SUMMON_STONE, VALLERIA);
		addTalkId(SOUL_SUMMON_STONE, VALLERIA);
		addKillId(SUMMONED_HARPAS, SUMMONED_GARP, SUMMONED_MORICKS);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, PlayerInstance player)
	{
		switch (event)
		{
			case "request_boss":
			{
				if ((BOSS != null) && !BOSS.isDead())
				{
					return "34434-4.html";
				}
				if (hasQuestItems(player, SOUL_QUARTZ))
				{
					takeItems(player, SOUL_QUARTZ, 1);
					int i = getRandom(100);
					if (i < 40)
					{
						BOSS = addSpawn(SUMMONED_HARPAS, player.getX() + getRandom(-300, 300), player.getY() + getRandom(-300, 300), player.getZ() + 10, getRandom(64000), false, 0, true);
						return "34434-1.html";
					}
					else if (i < 80)
					{
						BOSS = addSpawn(SUMMONED_GARP, player.getX() + getRandom(-300, 300), player.getY() + getRandom(-300, 300), player.getZ() + 10, getRandom(64000), false, 0, true);
						return "34434-2.html";
					}
					else
					{
						BOSS = addSpawn(SUMMONED_MORICKS, player.getX() + getRandom(-300, 300), player.getY() + getRandom(-300, 300), player.getZ() + 10, getRandom(64000), false, 0, true);
						return "34434-3.html";
					}
				}
				return "34434-5.html";
			}
		}
		return super.onAdvEvent(event, npc, player);
	}
	
	@Override
	public String onFirstTalk(Npc npc, PlayerInstance player)
	{
		if (npc.getId() == VALLERIA)
		{
			return "34435.html";
		}
		return "34434.html";
	}
	
	public static void main(String[] args)
	{
		new BlackbirdCampsite();
	}
}