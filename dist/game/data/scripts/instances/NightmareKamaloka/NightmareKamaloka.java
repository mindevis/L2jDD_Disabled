
package instances.NightmareKamaloka;

import java.util.HashMap;
import java.util.Map;

import org.l2jdd.gameserver.model.actor.Npc;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.model.holders.ItemHolder;
import org.l2jdd.gameserver.model.instancezone.Instance;

import instances.AbstractInstance;

/**
 * Nightmare Kamaloka instance zone.
 * @author St3eT
 */
public class NightmareKamaloka extends AbstractInstance
{
	// NPCs
	private static final int BENUSTA = 34542;
	private static final int DARK_RIDER = 26102;
	private static final int INVISIBLE_NPC = 18919;
	// Item
	private static final ItemHolder BENUSTAS_REWARD_BOX = new ItemHolder(81151, 1);
	// Skills
	// private static final int DARK_RIDER_UD = 16574;
	//@formatter:off
	private static final Map<Integer, Integer> BOSS_MAP = new HashMap<>();
	static
	{
		BOSS_MAP.put(26093, 18170002); // Mino
		BOSS_MAP.put(26094, 18170004); // Sola
		BOSS_MAP.put(26096, 18170006); // Ariarc
		BOSS_MAP.put(26099, 18170008); // Sirra
		BOSS_MAP.put(DARK_RIDER, -1); // Dark Rider
	}
	//@formatter:on
	// Misc
	private static final int TEMPLATE_ID = 258;
	
	public NightmareKamaloka()
	{
		super(TEMPLATE_ID);
		addStartNpc(BENUSTA);
		addTalkId(BENUSTA);
		addSpawnId(INVISIBLE_NPC);
		// addAttackId(DARK_RIDER_UD);
		addKillId(BOSS_MAP.keySet());
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, PlayerInstance player)
	{
		switch (event)
		{
			case "enterInstance":
			{
				enterInstance(player, npc, TEMPLATE_ID);
				break;
			}
			case "SPAWN_BOSSES":
			{
				final Instance instance = npc.getInstanceWorld();
				if (isInInstance(instance))
				{
					instance.spawnGroup("BOSSES");
				}
				break;
			}
		}
		return super.onAdvEvent(event, npc, player);
	}
	
	@Override
	public String onSpawn(Npc npc)
	{
		final Instance instance = npc.getInstanceWorld();
		if (isInInstance(instance) && (npc.getId() == INVISIBLE_NPC))
		{
			startQuestTimer("SPAWN_BOSSES", 10000, npc, null);
		}
		return super.onSpawn(npc);
	}
	
	@Override
	public String onKill(Npc npc, PlayerInstance killer, boolean isSummon)
	{
		final Instance instance = npc.getInstanceWorld();
		if (isInInstance(instance))
		{
			final int nextDoorId = BOSS_MAP.getOrDefault(npc.getId(), -1);
			if (nextDoorId == -1)
			{
				for (PlayerInstance member : instance.getPlayers())
				{
					giveItems(member, BENUSTAS_REWARD_BOX);
				}
				instance.finishInstance();
			}
			else
			{
				instance.openCloseDoor(nextDoorId, true);
			}
		}
		return super.onKill(npc, killer, isSummon);
	}
	
	/*
	 * @Override public String onAttack(Npc npc, PlayerInstance attacker, int damage, boolean isSummon) { final Instance instance = npc.getInstanceWorld(); if (isInInstance(instance)) { if (npc.getId() == DARK_RIDER_UD) { if ((npc.getCurrentHpPercent() >= 95) && npc.isScriptValue(0)) {
	 * npc.doCast(SkillData.getInstance().getSkill(DARK_RIDER_UD, 1)); npc.setScriptValue(1); } else if ((npc.getCurrentHpPercent() >= 75) && npc.isScriptValue(1)) { npc.doCast(SkillData.getInstance().getSkill(DARK_RIDER_UD, 2)); npc.setScriptValue(2); } else if ((npc.getCurrentHpPercent() >= 50) &&
	 * npc.isScriptValue(2)) { npc.doCast(SkillData.getInstance().getSkill(DARK_RIDER_UD, 3)); npc.setScriptValue(3); } else if ((npc.getCurrentHpPercent() >= 25) && npc.isScriptValue(3)) { npc.doCast(SkillData.getInstance().getSkill(DARK_RIDER_UD, 4)); npc.setScriptValue(4); } } } return
	 * super.onAttack(npc, attacker, damage, isSummon); }
	 */
	
	public static void main(String[] args)
	{
		new NightmareKamaloka();
	}
}