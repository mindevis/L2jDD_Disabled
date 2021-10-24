
package ai.areas.DenOfEvil;

import org.l2jdd.gameserver.ai.CtrlIntention;
import org.l2jdd.gameserver.enums.ChatType;
import org.l2jdd.gameserver.model.Location;
import org.l2jdd.gameserver.model.actor.Npc;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.model.holders.SkillHolder;
import org.l2jdd.gameserver.model.itemcontainer.Inventory;
import org.l2jdd.gameserver.network.NpcStringId;

import ai.AbstractNpcAI;

/**
 * Frightened Ragna Orc AI.
 * @author Gladicek, malyelfik
 */
public class FrightenedRagnaOrc extends AbstractNpcAI
{
	// NPC ID
	private static final int MOB_ID = 18807;
	// Chances
	private static final int ADENA = 10000;
	private static final int CHANCE = 1000;
	private static final int ADENA2 = 1000000;
	private static final int CHANCE2 = 10;
	// Skill
	private static final SkillHolder SKILL = new SkillHolder(6234, 1);
	
	private FrightenedRagnaOrc()
	{
		addAttackId(MOB_ID);
		addKillId(MOB_ID);
	}
	
	@Override
	public String onAttack(Npc npc, PlayerInstance attacker, int damage, boolean isSummon)
	{
		if (npc.isScriptValue(0))
		{
			npc.setScriptValue(1);
			startQuestTimer("say", (getRandom(5) + 3) * 1000, npc, null, true);
		}
		else if ((npc.getCurrentHp() < (npc.getMaxHp() * 0.2)) && npc.isScriptValue(1))
		{
			startQuestTimer("reward", 10000, npc, attacker);
			npc.broadcastSay(ChatType.NPC_GENERAL, NpcStringId.WAIT_WAIT_STOP_SAVE_ME_AND_I_LL_GIVE_YOU_10_000_000_ADENA);
			npc.setScriptValue(2);
		}
		return super.onAttack(npc, attacker, damage, isSummon);
	}
	
	@Override
	public String onKill(Npc npc, PlayerInstance player, boolean isSummon)
	{
		final NpcStringId msg = getRandomBoolean() ? NpcStringId.A_CURSE_UPON_YOU_2 : NpcStringId.I_REALLY_DIDN_T_WANT_TO_FIGHT;
		npc.broadcastSay(ChatType.NPC_GENERAL, msg);
		cancelQuestTimer("say", npc, null);
		cancelQuestTimer("reward", npc, player);
		return super.onKill(npc, player, isSummon);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, PlayerInstance player)
	{
		switch (event)
		{
			case "say":
			{
				if (npc.isDead() || !npc.isScriptValue(1))
				{
					cancelQuestTimer("say", npc, null);
					return null;
				}
				final NpcStringId msg = getRandomBoolean() ? NpcStringId.I_DON_T_WANT_TO_FIGHT : NpcStringId.IS_THIS_REALLY_NECESSARY;
				npc.broadcastSay(ChatType.NPC_GENERAL, msg);
				break;
			}
			case "reward":
			{
				if (!npc.isDead() && npc.isScriptValue(2))
				{
					if (getRandom(100000) < CHANCE2)
					{
						final NpcStringId msg = getRandomBoolean() ? NpcStringId.TH_THANKS_I_COULD_HAVE_BECOME_GOOD_FRIENDS_WITH_YOU : NpcStringId.I_LL_GIVE_YOU_10_000_000_ADENA_LIKE_I_PROMISED_I_MIGHT_BE_AN_ORC_WHO_KEEPS_MY_PROMISES;
						npc.broadcastSay(ChatType.NPC_GENERAL, msg);
						npc.setScriptValue(3);
						npc.doCast(SKILL.getSkill());
						for (int i = 0; i < 10; i++)
						{
							npc.dropItem(player, Inventory.ADENA_ID, ADENA2);
						}
					}
					else if (getRandom(100000) < CHANCE)
					{
						final NpcStringId msg = getRandomBoolean() ? NpcStringId.TH_THANKS_I_COULD_HAVE_BECOME_GOOD_FRIENDS_WITH_YOU : NpcStringId.SORRY_BUT_THIS_IS_ALL_I_HAVE_GIVE_ME_A_BREAK;
						npc.broadcastSay(ChatType.NPC_GENERAL, msg);
						npc.setScriptValue(3);
						npc.doCast(SKILL.getSkill());
						for (int i = 0; i < 10; i++)
						{
							npc.dropItem(player, Inventory.ADENA_ID, ADENA);
						}
					}
					else
					{
						final NpcStringId msg = getRandomBoolean() ? NpcStringId.THANKS_BUT_THAT_THING_ABOUT_10_000_000_ADENA_WAS_A_LIE_SEE_YA : NpcStringId.YOU_RE_PRETTY_DUMB_TO_BELIEVE_ME;
						npc.broadcastSay(ChatType.NPC_GENERAL, msg);
					}
					startQuestTimer("despawn", 1000, npc, null);
				}
				break;
			}
			case "despawn":
			{
				npc.setRunning();
				npc.getAI().setIntention(CtrlIntention.AI_INTENTION_MOVE_TO, new Location((npc.getX() + getRandom(-800, 800)), (npc.getY() + getRandom(-800, 800)), npc.getZ(), npc.getHeading()));
				npc.deleteMe();
				break;
			}
		}
		return null;
	}
	
	public static void main(String[] args)
	{
		new FrightenedRagnaOrc();
	}
}
