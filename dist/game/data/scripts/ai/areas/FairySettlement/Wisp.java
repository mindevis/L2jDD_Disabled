
package ai.areas.FairySettlement;

import org.l2jdd.gameserver.model.Location;
import org.l2jdd.gameserver.model.Spawn;
import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.actor.Creature;
import org.l2jdd.gameserver.model.actor.Npc;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.model.events.impl.creature.OnCreatureSee;
import org.l2jdd.gameserver.model.holders.SkillHolder;

import ai.AbstractNpcAI;

/**
 * Wisp AI.
 * @author St3eT
 */
public class Wisp extends AbstractNpcAI
{
	// NPCs
	private static final int WISP = 32915;
	private static final int LARGE_WISP = 32916;
	// Skills
	private static final SkillHolder WISP_HEAL = new SkillHolder(14064, 1);
	private static final SkillHolder LARGE_WISP_HEAL = new SkillHolder(14065, 1);
	// Misc
	private static final int RESPAWN_MIN = 60000;
	private static final int RESPAWN_MAX = 120000;
	
	private Wisp()
	{
		addSpawnId(WISP, LARGE_WISP);
		setCreatureSeeId(this::onCreatureSee, WISP, LARGE_WISP);
	}
	
	@Override
	public void onTimerEvent(String event, StatSet params, Npc npc, PlayerInstance player)
	{
		if (event.equals("DELETE_NPC"))
		{
			final StatSet statSet = new StatSet();
			statSet.set("LOCATION_OBJECT", npc.getLocation());
			getTimers().addTimer("RESPAWN_WISP_" + npc.getObjectId(), statSet, getRandom(RESPAWN_MIN, RESPAWN_MAX), null, null);
			npc.deleteMe();
		}
		else
		{
			addSpawn(WISP, params.getObject("LOCATION_OBJECT", Location.class));
		}
	}
	
	@Override
	public String onSpawn(Npc npc)
	{
		final Spawn spawn = npc.getSpawn();
		spawn.stopRespawn();
		
		if ((npc.getId() == WISP) && (getRandom(100) < 10))
		{
			addSpawn(LARGE_WISP, npc);
			npc.deleteMe();
		}
		else
		{
			npc.initSeenCreatures();
		}
		return super.onSpawn(npc);
	}
	
	public void onCreatureSee(OnCreatureSee event)
	{
		final Creature creature = event.getSeen();
		final Npc npc = (Npc) event.getSeer();
		if (creature.isPlayer() || creature.isFakePlayer())
		{
			npc.setTarget(creature);
			npc.doCast(npc.getId() == WISP ? WISP_HEAL.getSkill() : LARGE_WISP_HEAL.getSkill());
			getTimers().addTimer("DELETE_NPC", 5000, npc, null);
		}
	}
	
	public static void main(String[] args)
	{
		new Wisp();
	}
}