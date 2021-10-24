
package ai.areas.IsleOfPrayer;

import java.util.HashMap;
import java.util.Map;

import org.l2jdd.gameserver.model.actor.Npc;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.model.holders.ItemChanceHolder;

import ai.AbstractNpcAI;

/**
 * Isle of Prayer AI.
 * @author Zoey76
 */
public class IsleOfPrayer extends AbstractNpcAI
{
	// Items
	private static final int YELLOW_SEED_OF_EVIL_SHARD = 9593;
	private static final int GREEN_SEED_OF_EVIL_SHARD = 9594;
	private static final int BLUE_SEED_OF_EVIL_SHARD = 9595;
	private static final int RED_SEED_OF_EVIL_SHARD = 9596;
	// Monsters
	private static final Map<Integer, ItemChanceHolder> MONSTERS = new HashMap<>();
	
	static
	{
		MONSTERS.put(22257, new ItemChanceHolder(YELLOW_SEED_OF_EVIL_SHARD, 2087)); // Island Guardian
		MONSTERS.put(22258, new ItemChanceHolder(YELLOW_SEED_OF_EVIL_SHARD, 2147)); // White Sand Mirage
		MONSTERS.put(22259, new ItemChanceHolder(YELLOW_SEED_OF_EVIL_SHARD, 2642)); // Muddy Coral
		MONSTERS.put(22260, new ItemChanceHolder(YELLOW_SEED_OF_EVIL_SHARD, 2292)); // Kleopora
		MONSTERS.put(22261, new ItemChanceHolder(GREEN_SEED_OF_EVIL_SHARD, 1171)); // Seychelles
		MONSTERS.put(22262, new ItemChanceHolder(GREEN_SEED_OF_EVIL_SHARD, 1173)); // Naiad
		MONSTERS.put(22263, new ItemChanceHolder(GREEN_SEED_OF_EVIL_SHARD, 1403)); // Sonneratia
		MONSTERS.put(22264, new ItemChanceHolder(GREEN_SEED_OF_EVIL_SHARD, 1207)); // Castalia
		MONSTERS.put(22265, new ItemChanceHolder(RED_SEED_OF_EVIL_SHARD, 575)); // Chrysocolla
		MONSTERS.put(22266, new ItemChanceHolder(RED_SEED_OF_EVIL_SHARD, 493)); // Pythia
		MONSTERS.put(22267, new ItemChanceHolder(RED_SEED_OF_EVIL_SHARD, 770)); // Dark Water Dragon
		MONSTERS.put(22268, new ItemChanceHolder(BLUE_SEED_OF_EVIL_SHARD, 987)); // Shade
		MONSTERS.put(22269, new ItemChanceHolder(BLUE_SEED_OF_EVIL_SHARD, 995)); // Shade
		MONSTERS.put(22270, new ItemChanceHolder(BLUE_SEED_OF_EVIL_SHARD, 1008)); // Water Dragon Detractor
		MONSTERS.put(22271, new ItemChanceHolder(BLUE_SEED_OF_EVIL_SHARD, 1008)); // Water Dragon Detractor
	}
	
	private IsleOfPrayer()
	{
		addKillId(MONSTERS.keySet());
	}
	
	@Override
	public String onKill(Npc npc, PlayerInstance killer, boolean isSummon)
	{
		final ItemChanceHolder holder = MONSTERS.get(npc.getId());
		if (getRandom(10000) <= holder.getChance())
		{
			npc.dropItem(killer, holder);
		}
		return super.onKill(npc, killer, isSummon);
	}
	
	public static void main(String[] args)
	{
		new IsleOfPrayer();
	}
}
