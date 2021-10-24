
package ai.areas.SeedOfAnnihilation.Seyo;

import org.l2jdd.gameserver.enums.ChatType;
import org.l2jdd.gameserver.model.actor.Npc;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.network.NpcStringId;

import ai.AbstractNpcAI;

/**
 * Seyo AI.
 * @author St3eT
 */
public class Seyo extends AbstractNpcAI
{
	// NPC
	private static final int SEYO = 32737;
	// Item
	private static final int STONE_FRAGMENT = 15486; // Spirit Stone Fragment
	// Misc
	private static final NpcStringId[] TEXT =
	{
		NpcStringId.NO_ONE_ELSE_DON_T_WORRY_I_DON_T_BITE_HAHA,
		NpcStringId.OK_MASTER_OF_LUCK_THAT_S_YOU_HAHA_WELL_ANYONE_CAN_COME_AFTER_ALL,
		NpcStringId.SHEDDING_BLOOD_IS_A_GIVEN_ON_THE_BATTLEGROUND_AT_LEAST_IT_S_SAFE_HERE,
		NpcStringId.OK_WHO_S_NEXT_IT_ALL_DEPENDS_ON_YOUR_FATE_AND_LUCK_RIGHT_AT_LEAST_COME_AND_TAKE_A_LOOK,
		NpcStringId.THERE_WAS_SOMEONE_WHO_WON_10_000_FROM_ME_A_WARRIOR_SHOULDN_T_JUST_BE_GOOD_AT_FIGHTING_RIGHT_YOU_VE_GOTTA_BE_GOOD_IN_EVERYTHING
	};
	
	public Seyo()
	{
		addStartNpc(SEYO);
		addTalkId(SEYO);
		addFirstTalkId(SEYO);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, PlayerInstance player)
	{
		String htmltext = null;
		if (npc == null)
		{
			return htmltext;
		}
		switch (event)
		{
			case "TRICKERY_TIMER":
			{
				if (npc.isScriptValue(1))
				{
					npc.setScriptValue(0);
					npc.broadcastSay(ChatType.NPC_GENERAL, getRandomEntry(TEXT));
				}
				break;
			}
			case "give1":
			{
				if (npc.isScriptValue(1))
				{
					htmltext = "32737-04.html";
				}
				else if (!hasQuestItems(player, STONE_FRAGMENT))
				{
					htmltext = "32737-01.html";
				}
				else
				{
					npc.setScriptValue(1);
					takeItems(player, STONE_FRAGMENT, 1);
					if (getRandom(100) == 0)
					{
						giveItems(player, STONE_FRAGMENT, 100);
						npc.broadcastSay(ChatType.NPC_GENERAL, NpcStringId.AMAZING_S1_TOOK_100_OF_THESE_SOUL_STONE_FRAGMENTS_WHAT_A_COMPLETE_SWINDLER, player.getName());
					}
					else
					{
						npc.broadcastSay(ChatType.NPC_GENERAL, NpcStringId.HMM_HEY_DID_YOU_GIVE_S1_SOMETHING_BUT_IT_WAS_JUST_1_HAHA, player.getName());
					}
					startQuestTimer("TRICKERY_TIMER", 5000, npc, null);
				}
				break;
			}
			case "give5":
			{
				if (npc.isScriptValue(1))
				{
					htmltext = "32737-04.html";
				}
				else if (getQuestItemsCount(player, STONE_FRAGMENT) < 5)
				{
					htmltext = "32737-02.html";
				}
				else
				{
					npc.setScriptValue(1);
					takeItems(player, STONE_FRAGMENT, 5);
					final int chance = getRandom(100);
					if (chance < 20)
					{
						npc.broadcastSay(ChatType.NPC_GENERAL, NpcStringId.AHEM_S1_HAS_NO_LUCK_AT_ALL_TRY_PRAYING, player.getName());
					}
					else if (chance < 80)
					{
						giveItems(player, STONE_FRAGMENT, 1);
						npc.broadcastSay(ChatType.NPC_GENERAL, NpcStringId.IT_S_BETTER_THAN_LOSING_IT_ALL_RIGHT_OR_DOES_THIS_FEEL_WORSE);
					}
					else
					{
						final int itemCount = getRandom(10, 16);
						giveItems(player, STONE_FRAGMENT, itemCount);
						npc.broadcastSay(ChatType.NPC_GENERAL, NpcStringId.S1_PULLED_ONE_WITH_S2_DIGITS_LUCKY_NOT_BAD, player.getName(), String.valueOf(itemCount));
					}
					startQuestTimer("TRICKERY_TIMER", 5000, npc, null);
				}
				break;
			}
			case "give20":
			{
				if (npc.isScriptValue(1))
				{
					htmltext = "32737-04.html";
				}
				else if (getQuestItemsCount(player, STONE_FRAGMENT) < 20)
				{
					htmltext = "32737-03.html";
				}
				else
				{
					npc.setScriptValue(1);
					takeItems(player, STONE_FRAGMENT, 20);
					final int chance = getRandom(10000);
					if (chance == 0)
					{
						giveItems(player, STONE_FRAGMENT, 10000);
						npc.broadcastSay(ChatType.NPC_GENERAL, NpcStringId.AH_IT_S_OVER_WHAT_KIND_OF_GUY_IS_THAT_DAMN_FINE_YOU_S1_TAKE_IT_AND_GET_OUTTA_HERE, player.getName());
					}
					else if (chance < 10)
					{
						giveItems(player, STONE_FRAGMENT, 1);
						npc.broadcastSay(ChatType.NPC_GENERAL, NpcStringId.YOU_DON_T_FEEL_BAD_RIGHT_ARE_YOU_SAD_BUT_DON_T_CRY);
					}
					else
					{
						giveItems(player, STONE_FRAGMENT, getRandom(1, 100));
						npc.broadcastSay(ChatType.NPC_GENERAL, NpcStringId.A_BIG_PIECE_IS_MADE_UP_OF_LITTLE_PIECES_SO_HERE_S_A_LITTLE_PIECE);
					}
					startQuestTimer("TRICKERY_TIMER", 5000, npc, null);
				}
				break;
			}
		}
		return htmltext;
	}
	
	public static void main(String[] args)
	{
		new Seyo();
	}
}
