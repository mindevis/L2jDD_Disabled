
package ai.others;

import org.l2jdd.gameserver.ai.CtrlEvent;
import org.l2jdd.gameserver.enums.ChatType;
import org.l2jdd.gameserver.model.WorldObject;
import org.l2jdd.gameserver.model.actor.Attackable;
import org.l2jdd.gameserver.model.actor.Npc;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.network.NpcStringId;

import ai.AbstractNpcAI;

/**
 * Warrior Fishing Block AI.
 * @author Zoey76
 */
public class WarriorFishingBlock extends AbstractNpcAI
{
	// Monsters
	private static final int[] MONSTERS =
	{
		18319, // Caught Frog
		18320, // Caught Undine
		18321, // Caught Rakul
		18322, // Caught Sea Giant
		18323, // Caught Sea Horse Soldier
		18324, // Caught Homunculus
		18325, // Caught Flava
		18326, // Caught Gigantic Eye
	};
	// NPC Strings
	private static final NpcStringId[] NPC_STRINGS_ON_SPAWN =
	{
		NpcStringId.CROAK_CROAK_FOOD_LIKE_S1_IN_THIS_PLACE,
		NpcStringId.S1_HOW_LUCKY_I_AM,
		NpcStringId.PRAY_THAT_YOU_CAUGHT_A_WRONG_FISH_S1
	};
	private static final NpcStringId[] NPC_STRINGS_ON_ATTACK =
	{
		NpcStringId.DO_YOU_KNOW_WHAT_A_FROG_TASTES_LIKE,
		NpcStringId.I_WILL_SHOW_YOU_THE_POWER_OF_A_FROG,
		NpcStringId.I_WILL_SWALLOW_AT_A_MOUTHFUL
	};
	private static final NpcStringId[] NPC_STRINGS_ON_KILL =
	{
		NpcStringId.UGH_NO_CHANCE_HOW_COULD_THIS_ELDER_PASS_AWAY_LIKE_THIS,
		NpcStringId.CROAK_CROAK_A_FROG_IS_DYING,
		NpcStringId.A_FROG_TASTES_BAD_YUCK
	};
	// Misc
	private static final int CHANCE_TO_SHOUT_ON_ATTACK = 33;
	private static final int DESPAWN_TIME = 50; // 50 seconds to despawn
	
	public WarriorFishingBlock()
	{
		addAttackId(MONSTERS);
		addKillId(MONSTERS);
		addSpawnId(MONSTERS);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, PlayerInstance player)
	{
		switch (event)
		{
			case "SPAWN":
			{
				final WorldObject obj = npc.getTarget();
				if ((obj == null) || !obj.isPlayer())
				{
					npc.decayMe();
				}
				else
				{
					final PlayerInstance target = obj.getActingPlayer();
					npc.broadcastSay(ChatType.NPC_GENERAL, getRandomEntry(NPC_STRINGS_ON_SPAWN), target.getName());
					((Attackable) npc).addDamageHate(target, 0, 2000);
					npc.getAI().notifyEvent(CtrlEvent.EVT_ATTACKED, target);
					npc.addAttackerToAttackByList(target);
					
					startQuestTimer("DESPAWN", DESPAWN_TIME * 1000, npc, target);
				}
				break;
			}
			case "DESPAWN":
			{
				npc.decayMe();
				break;
			}
		}
		return super.onAdvEvent(event, npc, player);
	}
	
	@Override
	public String onAttack(Npc npc, PlayerInstance attacker, int damage, boolean isSummon)
	{
		if (getRandom(100) < CHANCE_TO_SHOUT_ON_ATTACK)
		{
			npc.broadcastSay(ChatType.NPC_GENERAL, getRandomEntry(NPC_STRINGS_ON_ATTACK));
		}
		return super.onAttack(npc, attacker, damage, isSummon);
	}
	
	@Override
	public String onKill(Npc npc, PlayerInstance killer, boolean isSummon)
	{
		npc.broadcastSay(ChatType.NPC_GENERAL, getRandomEntry(NPC_STRINGS_ON_KILL));
		cancelQuestTimer("DESPAWN", npc, killer);
		return super.onKill(npc, killer, isSummon);
	}
	
	@Override
	public String onSpawn(Npc npc)
	{
		cancelQuestTimer("SPAWN", npc, null);
		cancelQuestTimer("DESPAWN", npc, null);
		startQuestTimer("SPAWN", 2000, npc, null);
		return super.onSpawn(npc);
	}
	
	public static void main(String[] args)
	{
		new WarriorFishingBlock();
	}
}
