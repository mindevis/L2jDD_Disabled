
package ai.areas.KeucereusAllianceBase;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.l2jdd.gameserver.enums.ChatType;
import org.l2jdd.gameserver.model.Spawn;
import org.l2jdd.gameserver.model.actor.Npc;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.network.NpcStringId;

import ai.AbstractNpcAI;

/**
 * Dilios AI
 * @author JIV, Sephiroth, Apocalipce
 */
public class GeneralDilios extends AbstractNpcAI
{
	private static final int GENERAL_ID = 32549;
	private static final int GUARD_ID = 32619;
	
	private Npc _general = null;
	private final Set<Spawn> _guards = Collections.newSetFromMap(new ConcurrentHashMap<>());
	
	private static final NpcStringId[] DILIOS_TEXT =
	{
		NpcStringId.SPREAD_THE_WORD_HEROES_YOUNG_AND_OLD_ARE_GATHERING_TO_MARCH_TO_THE_SEED_OF_DESTRUCTION_AND_TAKE_DOWN_TIAT_ONCE_AND_FOR_ALL,
		// NpcStringId.MESSENGER_INFORM_THE_PATRONS_OF_THE_KEUCEREUS_ALLIANCE_BASE_THE_SEED_OF_DESTRUCTION_IS_CURRENTLY_SECURED_UNDER_THE_FLAG_OF_THE_KEUCEREUS_ALLIANCE,
		// NpcStringId.MESSENGER_INFORM_THE_PATRONS_OF_THE_KEUCEREUS_ALLIANCE_BASE_TIATS_MOUNTED_TROOP_IS_CURRENTLY_TRYING_TO_RETAKE_SEED_OF_DESTRUCTION_COMMIT_ALL_THE_AVAILABLE_REINFORCEMENTS_INTO_SEED_OF_DESTRUCTION,
		NpcStringId.SPREAD_THE_WORD_BRAVE_WARRIORS_HAVE_STORMED_THE_HALL_OF_SUFFERING_AND_ARE_MARCHING_ONTO_THE_HALL_OF_EROSION_IN_THE_SEED_OF_INFINITY,
		// NpcStringId.MESSENGER_INFORM_THE_BROTHERS_IN_KUCEREUS_CLAN_OUTPOST_SWEEPING_THE_SEED_OF_INFINITY_IS_CURRENTLY_COMPLETE_TO_THE_HEART_OF_THE_SEED_EKIMUS_IS_BEING_DIRECTLY_ATTACKED_AND_THE_UNDEAD_REMAINING_IN_THE_HALL_OF_SUFFERING_ARE_BEING_ERADICATED,
		NpcStringId.SPREAD_THE_WORD_THE_FLAG_OF_THE_KEUCEREUS_ALLIANCE_FLIES_PROUDLY_OVER_THE_SEED_OF_INFINITY
		// NpcStringId.MESSENGER_INFORM_THE_PATRONS_OF_THE_KEUCEREUS_ALLIANCE_BASE_THE_RESURRECTED_UNDEAD_IN_THE_SEED_OF_INFINITY_ARE_POURING_INTO_THE_HALL_OF_SUFFERING_AND_THE_HALL_OF_EROSION
		// NpcStringId.MESSENGER_INFORM_THE_BROTHERS_IN_KUCEREUS_CLAN_OUTPOST_EKIMUS_IS_ABOUT_TO_BE_REVIVED_BY_THE_RESURRECTED_UNDEAD_IN_SEED_OF_INFINITY_SEND_ALL_REINFORCEMENTS_TO_THE_HEART_AND_THE_HALL_OF_SUFFERING
	};
	
	public GeneralDilios()
	{
		addSpawnId(GENERAL_ID, GUARD_ID);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, PlayerInstance player)
	{
		if (event.startsWith("command_"))
		{
			int value = Integer.parseInt(event.substring(8));
			if (value < 6)
			{
				_general.broadcastSay(ChatType.NPC_GENERAL, NpcStringId.STABBING_THREE_TIMES);
				startQuestTimer("guard_animation_0", 3400, null, null);
			}
			else
			{
				value = -1;
				_general.broadcastSay(ChatType.NPC_SHOUT, getRandomEntry(DILIOS_TEXT));
			}
			startQuestTimer("command_" + (value + 1), 60000, null, null);
		}
		else if (event.startsWith("guard_animation_"))
		{
			final int value = Integer.parseInt(event.substring(16));
			for (Spawn guard : _guards)
			{
				guard.getLastSpawn().broadcastSocialAction(4);
			}
			if (value < 2)
			{
				startQuestTimer("guard_animation_" + (value + 1), 1500, null, null);
			}
		}
		return super.onAdvEvent(event, npc, player);
	}
	
	@Override
	public String onSpawn(Npc npc)
	{
		if (npc.getId() == GENERAL_ID)
		{
			startQuestTimer("command_0", 60000, null, null);
			_general = npc;
		}
		else if (npc.getId() == GUARD_ID)
		{
			_guards.add(npc.getSpawn());
		}
		return super.onSpawn(npc);
	}
	
	public static void main(String[] args)
	{
		new GeneralDilios();
	}
}
