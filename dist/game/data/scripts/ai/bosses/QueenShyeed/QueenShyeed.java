
package ai.bosses.QueenShyeed;

import org.l2jdd.commons.util.Chronos;
import org.l2jdd.gameserver.enums.ChatType;
import org.l2jdd.gameserver.instancemanager.GlobalVariablesManager;
import org.l2jdd.gameserver.instancemanager.ZoneManager;
import org.l2jdd.gameserver.model.Location;
import org.l2jdd.gameserver.model.actor.Npc;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.model.zone.type.EffectZone;
import org.l2jdd.gameserver.network.NpcStringId;

import ai.AbstractNpcAI;

/**
 * Queen Shyeed AI
 * @author malyelfik
 */
public class QueenShyeed extends AbstractNpcAI
{
	// NPC
	private static final int SHYEED = 25671;
	private static final Location SHYEED_LOC = new Location(79634, -55428, -6104, 0);
	
	// Respawn
	private static final int RESPAWN = 86400000; // 24 h
	private static final int RANDOM_RESPAWN = 43200000; // 12 h
	
	// Zones
	// private static final EffectZone MOB_BUFF_ZONE = ZoneManager.getInstance().getZoneById(200103, EffectZone.class);
	private static final EffectZone MOB_BUFF_DISPLAY_ZONE = ZoneManager.getInstance().getZoneById(200104, EffectZone.class);
	private static final EffectZone PC_BUFF_ZONE = ZoneManager.getInstance().getZoneById(200105, EffectZone.class);
	
	@Override
	public String onAdvEvent(String event, Npc npc, PlayerInstance player)
	{
		switch (event)
		{
			case "respawn":
			{
				spawnShyeed();
				break;
			}
			case "despawn":
			{
				if (!npc.isDead())
				{
					startRespawn();
					npc.deleteMe();
				}
				break;
			}
		}
		return null;
	}
	
	@Override
	public String onKill(Npc npc, PlayerInstance killer, boolean isSummon)
	{
		npc.broadcastSay(ChatType.NPC_GENERAL, NpcStringId.SHYEED_S_CRY_IS_STEADILY_DYING_DOWN);
		startRespawn();
		PC_BUFF_ZONE.setEnabled(true);
		return super.onKill(npc, killer, isSummon);
	}
	
	private QueenShyeed()
	{
		addKillId(SHYEED);
		spawnShyeed();
	}
	
	private void spawnShyeed()
	{
		final long respawn = GlobalVariablesManager.getInstance().getLong("QueenShyeedRespawn", 0);
		final long remain = respawn != 0 ? respawn - Chronos.currentTimeMillis() : 0;
		if (remain > 0)
		{
			startQuestTimer("respawn", remain, null, null);
			return;
		}
		final Npc npc = addSpawn(SHYEED, SHYEED_LOC, false, 0);
		startQuestTimer("despawn", 10800000, npc, null);
		PC_BUFF_ZONE.setEnabled(false);
		// MOB_BUFF_ZONE.setEnabled(true);
		MOB_BUFF_DISPLAY_ZONE.setEnabled(true);
	}
	
	private void startRespawn()
	{
		final int respawnTime = RESPAWN - getRandom(RANDOM_RESPAWN);
		GlobalVariablesManager.getInstance().set("QueenShyeedRespawn", Long.toString(Chronos.currentTimeMillis() + respawnTime));
		startQuestTimer("respawn", respawnTime, null, null);
		// MOB_BUFF_ZONE.setEnabled(false);
		MOB_BUFF_DISPLAY_ZONE.setEnabled(false);
	}
	
	public static void main(String[] args)
	{
		new QueenShyeed();
	}
}
