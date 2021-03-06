
package ai.areas.GardenOfGenesis.Apherus;

import org.l2jdd.gameserver.enums.TeleportWhereType;
import org.l2jdd.gameserver.model.World;
import org.l2jdd.gameserver.model.actor.Creature;
import org.l2jdd.gameserver.model.actor.Npc;
import org.l2jdd.gameserver.model.actor.instance.MonsterInstance;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.model.holders.SkillHolder;
import org.l2jdd.gameserver.model.zone.ZoneType;
import org.l2jdd.gameserver.network.NpcStringId;
import org.l2jdd.gameserver.network.serverpackets.ExShowScreenMessage;

import ai.AbstractNpcAI;

/**
 * Apherus RB
 * @author Gigi
 */
public class Apherus extends AbstractNpcAI
{
	private static final int APHERUS = 25775;
	private static final int APHERUS_SUBORDINATE = 25865;
	private static final int[] APHERUS_DOOR_NPCS =
	{
		33133,
		33134,
		33135,
		33136
	};
	private static int[] APHERUS_DOORS =
	{
		26210041,
		26210042,
		26210043,
		26210044
	};
	private static final int[] APHERUS_DOOR_GUARD =
	{
		25776,
		25777,
		25778
	};
	private static final SkillHolder GARDEN_APHERUS_RECOVERY = new SkillHolder(14088, 1);
	private static final SkillHolder APHERUS_INVICIBILITY = new SkillHolder(14201, 1);
	private static final int APERUS_KEY = 17373;
	private static final int APHERUS_ZONE_ID = 60060;
	private static boolean _doorIsOpen = false;
	
	public Apherus()
	{
		addAttackId(APHERUS);
		addKillId(APHERUS);
		addSpawnId(APHERUS);
		addTalkId(APHERUS_DOOR_NPCS);
		addStartNpc(APHERUS_DOOR_NPCS);
		addFirstTalkId(APHERUS_DOOR_NPCS);
		addEnterZoneId(APHERUS_ZONE_ID);
		addExitZoneId(APHERUS_ZONE_ID);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, PlayerInstance player)
	{
		if (event.equals("buff"))
		{
			World.getInstance().forEachVisibleObjectInRange(npc, Npc.class, 3000, apherus ->
			{
				if ((apherus.getId() == APHERUS))
				{
					apherus.stopSkillEffects(APHERUS_INVICIBILITY.getSkill());
				}
			});
			return null;
		}
		return event;
	}
	
	@Override
	public String onAttack(Npc npc, PlayerInstance attacker, int damage, boolean isSummon)
	{
		if ((getRandom(120) < 2) && !npc.isDead())
		{
			final Npc minions = addSpawn(APHERUS_SUBORDINATE, npc.getX() + getRandom(-30, 30), npc.getY() + getRandom(-30, 30), npc.getZ(), npc.getHeading(), true, 300000, false);
			addAttackPlayerDesire(minions, attacker);
		}
		return super.onAttack(npc, attacker, damage, isSummon);
	}
	
	@Override
	public String onKill(Npc npc, PlayerInstance killer, boolean isSummon)
	{
		World.getInstance().forEachVisibleObjectInRange(npc, Npc.class, 1500, minion ->
		{
			if (minion.getId() == APHERUS_SUBORDINATE)
			{
				minion.deleteMe();
			}
		});
		return super.onKill(npc, killer, isSummon);
	}
	
	@Override
	public String onSpawn(Npc npc)
	{
		APHERUS_INVICIBILITY.getSkill().applyEffects(npc, npc);
		_doorIsOpen = false;
		for (int door : APHERUS_DOORS)
		{
			closeDoor(door, npc.getInstanceId());
		}
		return super.onSpawn(npc);
	}
	
	@Override
	public String onEnterZone(Creature creature, ZoneType zone)
	{
		if (creature.isRaid())
		{
			creature.stopSkillEffects(GARDEN_APHERUS_RECOVERY.getSkill());
		}
		else if (creature.isPlayable() && !_doorIsOpen && !creature.isGM())
		{
			creature.teleToLocation(TeleportWhereType.TOWN);
		}
		return super.onEnterZone(creature, zone);
	}
	
	@Override
	public String onExitZone(Creature creature, ZoneType zone)
	{
		if (creature.isRaid())
		{
			GARDEN_APHERUS_RECOVERY.getSkill().applyEffects(creature, creature);
		}
		return super.onExitZone(creature, zone);
	}
	
	@Override
	public String onFirstTalk(Npc npc, PlayerInstance player)
	{
		if (!_doorIsOpen)
		{
			if (!player.destroyItemByItemId("Apherus", APERUS_KEY, 1, player, true))
			{
				return "apherusDoor-no.html";
			}
			if (getRandom(100) > 60)
			{
				startQuestTimer("buff", 500, npc, player);
				for (int door : APHERUS_DOORS)
				{
					openDoor(door, npc.getInstanceId());
				}
				npc.broadcastPacket(new ExShowScreenMessage(NpcStringId.APHERUS_GARDEN_DOOR_WAS_OPENED, ExShowScreenMessage.TOP_CENTER, 3000, true));
			}
			else
			{
				for (int i = 0; i < 4; i++)
				{
					final MonsterInstance protector = (MonsterInstance) addSpawn(APHERUS_DOOR_GUARD[getRandom(APHERUS_DOOR_GUARD.length)], player.getX() + getRandom(10, 30), player.getY() + getRandom(10, 30), player.getZ(), 0, false, 600000, false);
					protector.setRunning();
					protector.setTarget(player);
					addAttackPlayerDesire(protector, player);
				}
				showOnScreenMsg(player, NpcStringId.S1_THE_KEY_DOES_NOT_MATCH_SO_WE_RE_IN_TROUBLE, ExShowScreenMessage.TOP_CENTER, 6000, true, player.getName());
			}
		}
		else
		{
			return "apherusDoor-no.html";
		}
		return null;
	}
	
	public static void main(String[] args)
	{
		new Apherus();
	}
}