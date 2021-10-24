
package instances.KartiasLabyrinth;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.l2jdd.commons.util.CommonUtil;
import org.l2jdd.gameserver.enums.ChatType;
import org.l2jdd.gameserver.enums.InstanceType;
import org.l2jdd.gameserver.model.Location;
import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.WorldObject;
import org.l2jdd.gameserver.model.actor.Creature;
import org.l2jdd.gameserver.model.actor.Npc;
import org.l2jdd.gameserver.model.actor.instance.FriendlyNpcInstance;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.model.events.impl.creature.OnCreatureDeath;
import org.l2jdd.gameserver.model.events.impl.instance.OnInstanceStatusChange;
import org.l2jdd.gameserver.model.holders.SkillHolder;
import org.l2jdd.gameserver.model.instancezone.Instance;
import org.l2jdd.gameserver.network.NpcStringId;
import org.l2jdd.gameserver.util.Util;

import ai.AbstractNpcAI;

/**
 * Kartia Helper Elise AI. Healer
 * @author flanagak
 */
public class KartiaHelperElise extends AbstractNpcAI
{
	// NPCs
	private static final int[] KARTIA_ELISE =
	{
		33617, // Elise (Kartia 85)
		33628, // Elise (Kartia 90)
		33639, // Elise (Kartia 95)
	};
	private static final int[] KARTIA_ADOLPH =
	{
		33609, // Adolph (Kartia 85)
		33620, // Adolph (Kartia 90)
		33631, // Adolph (Kartia 95)
	};
	private static final int[] KARTIA_BARTON =
	{
		33611, // Barton (Kartia 85)
		33622, // Barton (Kartia 90)
		33633, // Barton (Kartia 95)
	};
	private static final int[] KARTIA_ELIYAH =
	{
		33615, // Eliyah (Kartia 85)
		33626, // Eliyah (Kartia 90)
		33637, // Eliyah (Kartia 95)
	};
	private static final int[] KARTIA_HAYUK =
	{
		33613, // Hayuk (Kartia 85)
		33624, // Hayuk (Kartia 90)
		33635, // Hayuk (Kartia 95)
	};
	private static final int[] KARTIA_FRIENDS =
	{
		33617, // Elise (Kartia 85)
		33628, // Elise (Kartia 90)
		33639, // Elise (Kartia 95)
		33609, // Adolph (Kartia 85)
		33620, // Adolph (Kartia 90)
		33631, // Adolph (Kartia 95)
		33611, // Barton (Kartia 85)
		33622, // Barton (Kartia 90)
		33633, // Barton (Kartia 95)
		33615, // Eliyah (Kartia 85)
		33626, // Eliyah (Kartia 90)
		33637, // Eliyah (Kartia 95)
		33613, // Hayuk (Kartia 85)
		33624, // Hayuk (Kartia 90)
		33635, // Hayuk (Kartia 95)
		33618, // Eliyah's Guardian Spirit (Kartia 85)
		33629, // Eliyah's Guardian Spirit (Kartia 90)
		33640, // Eliyah's Guardian Spirit (Kartia 95)
	};
	// Misc
	private static final int[] KARTIA_SOLO_INSTANCES =
	{
		205, // Solo 85
		206, // Solo 90
		207, // Solo 95
	};
	
	private KartiaHelperElise()
	{
		setInstanceStatusChangeId(this::onInstanceStatusChange, KARTIA_SOLO_INSTANCES);
		addSeeCreatureId(KARTIA_ELISE);
		setCreatureKillId(this::onCreatureKill, KARTIA_ELISE);
	}
	
	@Override
	public void onTimerEvent(String event, StatSet params, Npc npc, PlayerInstance player)
	{
		final Instance instance = npc.getInstanceWorld();
		if ((instance != null) && event.equals("CHECK_ACTION"))
		{
			final StatSet npcVars = npc.getVariables();
			final FriendlyNpcInstance adolph = npcVars.getObject("ADOLPH_OBJECT", FriendlyNpcInstance.class);
			if (!npc.isCastingNow())
			{
				healFriends(npc);
			}
			if (adolph != null)
			{
				final double distance = npc.calculateDistance2D(adolph);
				if (distance > 300)
				{
					final Location loc = new Location(adolph.getX(), adolph.getY(), adolph.getZ() + 50);
					final Location randLoc = new Location(loc.getX() + getRandom(-100, 100), loc.getY() + getRandom(-100, 100), loc.getZ());
					if (distance > 600)
					{
						npc.teleToLocation(loc);
					}
					else
					{
						npc.setRunning();
					}
					addMoveToDesire(npc, randLoc, 23);
				}
			}
			npc.setTarget(npc);
		}
	}
	
	private void healFriends(Npc npc)
	{
		final Instance instance = npc.getInstanceWorld();
		if (instance != null)
		{
			final StatSet npcVars = npc.getVariables();
			final StatSet instParams = instance.getTemplateParameters();
			if (!npc.isCastingNow())
			{
				final PlayerInstance plr = npcVars.getObject("PLAYER_OBJECT", PlayerInstance.class);
				final SkillHolder progressiveHeal = instParams.getSkillHolder("eliseProgressiveHeal"); // AOE heal
				final SkillHolder radiantHeal = instParams.getSkillHolder("eliseRadiantHeal"); // Single target heal
				final SkillHolder recharge = instParams.getSkillHolder("eliseRecharge");
				
				// Get HP percentage for all friends
				final Map<WorldObject, Integer> hpMap = new HashMap<>();
				instance.getAliveNpcs(KARTIA_FRIENDS).forEach(friend -> hpMap.put(friend, friend != null ? friend.getCurrentHpPercent() : 100));
				hpMap.put(plr, plr != null ? plr.getCurrentHpPercent() : 100);
				final Map<WorldObject, Integer> sortedHpMap = Util.sortByValue(hpMap, false);
				
				// See if any friends are below 80% HP and add to list of people to heal.
				final List<WorldObject> peopleToHeal = new ArrayList<>();
				for (Entry<WorldObject, Integer> entry : sortedHpMap.entrySet())
				{
					final WorldObject friend = entry.getKey();
					final int percent = entry.getValue().intValue();
					if ((friend != null) && (percent < 80) && (percent > 1))
					{
						peopleToHeal.add(friend);
					}
				}
				
				if (!peopleToHeal.isEmpty())
				{
					// At least one friend was below 80% HP.
					if (peopleToHeal.size() > 1)
					{
						// Helper NPC AOE skills affecting monsters so skill power has set to 0.
						// Using skill is just for animation. Need to heal each NPC/Player manually.
						npc.setTarget(npc);
						npc.broadcastSay(ChatType.NPC_GENERAL, NpcStringId.POWER_OF_LIGHT_PROTECT_US);
						npc.doCast(progressiveHeal.getSkill(), null, true, false);
						npc.setTarget(npc);
						for (WorldObject personToHeal : peopleToHeal)
						{
							if (personToHeal.getInstanceType() == InstanceType.PlayerInstance)
							{
								final PlayerInstance thePlayer = (PlayerInstance) personToHeal;
								thePlayer.setCurrentHp((thePlayer.getMaxHp() * .20) + thePlayer.getCurrentHp());
							}
							else
							{
								final Npc npcToHeal = (Npc) personToHeal;
								npcToHeal.setCurrentHp((npcToHeal.getMaxHp() * .20) + npcToHeal.getCurrentHp());
							}
						}
					}
					else
					{
						// Only one person needs cure. Cast single target heal
						for (WorldObject personToHeal : peopleToHeal)
						{
							npc.setTarget(personToHeal);
							npc.broadcastSay(ChatType.NPC_GENERAL, NpcStringId.POWER_OF_LIGHT_PROTECT_US);
							npc.doCast(radiantHeal.getSkill(), null, true, false);
							npc.setTarget(npc);
						}
					}
				}
				else
				{
					// No one needs healing. Check if player character needs recharge.
					if ((plr != null) && !plr.isDead() && (plr.getCurrentMpPercent() < 50))
					{
						npc.setTarget(plr);
						npc.broadcastSay(ChatType.NPC_GENERAL, NpcStringId.ELECTRIFYING_RECHARGE);
						npc.doCast(recharge.getSkill(), null, true, false);
						npc.setTarget(npc);
					}
				}
			}
		}
	}
	
	public void onInstanceStatusChange(OnInstanceStatusChange event)
	{
		final Instance instance = event.getWorld();
		final int status = event.getStatus();
		if (status == 1)
		{
			instance.getAliveNpcs(KARTIA_ELISE).forEach(elise -> getTimers().addRepeatingTimer("CHECK_ACTION", 3000, elise, null));
		}
	}
	
	@Override
	public String onSeeCreature(Npc npc, Creature creature, boolean isSummon)
	{
		if (creature.isPlayer() || (creature instanceof FriendlyNpcInstance))
		{
			final StatSet npcVars = npc.getVariables();
			if (creature.isPlayer())
			{
				npcVars.set("PLAYER_OBJECT", creature.getActingPlayer());
			}
			else if (CommonUtil.contains(KARTIA_ADOLPH, creature.getId()))
			{
				npcVars.set("ADOLPH_OBJECT", creature);
			}
			else if (CommonUtil.contains(KARTIA_BARTON, creature.getId()))
			{
				npcVars.set("BARTON_OBJECT", creature);
			}
			else if (CommonUtil.contains(KARTIA_ELIYAH, creature.getId()))
			{
				npcVars.set("ELIYAH_OBJECT", creature);
			}
			else if (CommonUtil.contains(KARTIA_HAYUK, creature.getId()))
			{
				npcVars.set("HAYUK_OBJECT", creature);
			}
		}
		return super.onSeeCreature(npc, creature, isSummon);
	}
	
	public void onCreatureKill(OnCreatureDeath event)
	{
		final Npc npc = (Npc) event.getTarget();
		final Instance world = npc.getInstanceWorld();
		if (world != null)
		{
			getTimers().cancelTimersOf(npc);
			npc.doDie(event.getAttacker());
		}
	}
	
	public static void main(String[] args)
	{
		new KartiaHelperElise();
	}
}