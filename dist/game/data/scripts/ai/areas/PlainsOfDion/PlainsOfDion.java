
package ai.areas.PlainsOfDion;

import org.l2jdd.commons.util.CommonUtil;
import org.l2jdd.gameserver.enums.ChatType;
import org.l2jdd.gameserver.geoengine.GeoEngine;
import org.l2jdd.gameserver.model.World;
import org.l2jdd.gameserver.model.actor.Npc;
import org.l2jdd.gameserver.model.actor.instance.MonsterInstance;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.network.NpcStringId;

import ai.AbstractNpcAI;

/**
 * AI for mobs in Plains of Dion (near Floran Village).
 * @author Gladicek
 */
public class PlainsOfDion extends AbstractNpcAI
{
	private static final int[] DELU_LIZARDMEN =
	{
		21104, // Delu Lizardman Supplier
		21105, // Delu Lizardman Special Agent
		21107, // Delu Lizardman Commander
	};
	
	private static final NpcStringId[] MONSTERS_MSG =
	{
		NpcStringId.S1_HOW_DARE_YOU_INTERRUPT_OUR_FIGHT_HEY_GUYS_HELP,
		NpcStringId.S1_HEY_WE_RE_HAVING_A_DUEL_HERE,
		NpcStringId.THE_DUEL_IS_OVER_ATTACK,
		NpcStringId.FOUL_KILL_THE_COWARD,
		NpcStringId.HOW_DARE_YOU_INTERRUPT_A_SACRED_DUEL_YOU_MUST_BE_TAUGHT_A_LESSON
	};
	
	private static final NpcStringId[] MONSTERS_ASSIST_MSG =
	{
		NpcStringId.DIE_YOU_COWARD,
		NpcStringId.KILL_THE_COWARD,
		NpcStringId.WHAT_ARE_YOU_LOOKING_AT
	};
	
	private PlainsOfDion()
	{
		addAttackId(DELU_LIZARDMEN);
	}
	
	@Override
	public String onAttack(Npc npc, PlayerInstance player, int damage, boolean isSummon)
	{
		if (npc.isScriptValue(0))
		{
			final int i = getRandom(5);
			if (i < 2)
			{
				npc.broadcastSay(ChatType.NPC_GENERAL, MONSTERS_MSG[i], player.getName());
			}
			else
			{
				npc.broadcastSay(ChatType.NPC_GENERAL, MONSTERS_MSG[i]);
			}
			
			World.getInstance().forEachVisibleObjectInRange(npc, MonsterInstance.class, npc.getTemplate().getClanHelpRange(), obj ->
			{
				if (CommonUtil.contains(DELU_LIZARDMEN, obj.getId()) && !obj.isAttackingNow() && !obj.isDead() && GeoEngine.getInstance().canSeeTarget(npc, obj))
				{
					addAttackPlayerDesire(obj, player);
					obj.broadcastSay(ChatType.NPC_GENERAL, MONSTERS_ASSIST_MSG[getRandom(3)]);
				}
			});
			npc.setScriptValue(1);
		}
		return super.onAttack(npc, player, damage, isSummon);
	}
	
	public static void main(String[] args)
	{
		new PlainsOfDion();
	}
}
