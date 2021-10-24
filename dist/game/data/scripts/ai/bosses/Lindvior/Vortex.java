
package ai.bosses.Lindvior;

import java.util.Collection;

import org.l2jdd.gameserver.ai.CtrlIntention;
import org.l2jdd.gameserver.geoengine.GeoEngine;
import org.l2jdd.gameserver.model.Location;
import org.l2jdd.gameserver.model.World;
import org.l2jdd.gameserver.model.actor.Creature;
import org.l2jdd.gameserver.model.actor.Npc;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.network.serverpackets.FlyToLocation;
import org.l2jdd.gameserver.network.serverpackets.ValidateLocation;

import ai.AbstractNpcAI;

/**
 * Vortex AI
 * @author Gigi
 * @date 2017-07-23 - [10:32:50]
 */
public class Vortex extends AbstractNpcAI
{
	private static final int SMALL_VORTEX = 25898;
	private static final int BIG_VORTEX = 19427;
	
	public Vortex()
	{
		super();
		addSeeCreatureId(SMALL_VORTEX, BIG_VORTEX);
		addSpawnId(SMALL_VORTEX, BIG_VORTEX);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, PlayerInstance player)
	{
		switch (event)
		{
			case "rnd_small":
			{
				World.getInstance().forEachVisibleObjectInRange(npc, PlayerInstance.class, 250, attackers ->
				{
					if ((attackers != null) && !attackers.isDead() && !attackers.isAlikeDead())
					{
						attackers.getAI().setIntention(CtrlIntention.AI_INTENTION_IDLE);
						final int radians = (int) Math.toRadians(npc.calculateDirectionTo(attackers));
						final int x = (int) (attackers.getX() + (600 * Math.cos(radians)));
						final int y = (int) (attackers.getY() + (600 * Math.sin(radians)));
						final int z = attackers.getZ();
						final Location loc = GeoEngine.getInstance().getValidLocation(attackers.getX(), attackers.getY(), attackers.getZ(), x, y, z, attackers.getInstanceWorld());
						attackers.broadcastPacket(new FlyToLocation(attackers, x, y, z, FlyToLocation.FlyType.THROW_UP, 800, 800, 800));
						attackers.setXYZ(loc);
						attackers.broadcastPacket(new ValidateLocation(attackers));
						npc.getAI().setIntention(CtrlIntention.AI_INTENTION_ATTACK, player);
						startQuestTimer("stop_knock_down", 5000, npc, attackers);
						startQuestTimer("despawn_small", 5000, npc, null);
					}
				});
				break;
			}
			case "rnd_big":
			{
				World.getInstance().forEachVisibleObjectInRange(npc, PlayerInstance.class, 500, attackers ->
				{
					if ((attackers != null) && !attackers.isDead() && !attackers.isAlikeDead())
					{
						attackers.setCurrentHp(attackers.getMaxHp() * 0.2);
						attackers.setCurrentMp(attackers.getMaxMp() * 0.2);
						attackers.setCurrentCp(1.0);
						startQuestTimer("despawn_big", 60000, npc, null);
					}
				});
				break;
			}
			case "despawn_small":
			{
				if (npc != null)
				{
					cancelQuestTimers("rnd_small");
					npc.getSpawn().stopRespawn();
					npc.doDie(null);
				}
				break;
			}
			case "despawn_big":
			{
				if (npc != null)
				{
					cancelQuestTimers("despawn_big");
					npc.getSpawn().stopRespawn();
					npc.deleteMe();
				}
				break;
			}
		}
		return super.onAdvEvent(event, npc, player);
	}
	
	@Override
	public String onSeeCreature(Npc npc, Creature creature, boolean isSummon)
	{
		switch (npc.getId())
		{
			case SMALL_VORTEX:
			{
				startQuestTimer("rnd_small", 5000, npc, null, true);
				break;
			}
			case BIG_VORTEX:
			{
				startQuestTimer("rnd_big", 10000, npc, null, true);
				break;
			}
		}
		return super.onSeeCreature(npc, creature, isSummon);
	}
	
	@Override
	public String onSpawn(Npc npc)
	{
		switch (npc.getId())
		{
			case SMALL_VORTEX:
			{
				attackRandomTarget(npc);
				npc.setRandomWalking(true);
				npc.setRunning();
				break;
			}
			case BIG_VORTEX:
			{
				attackRandomTarget(npc);
				npc.setRandomWalking(true);
				npc.setRunning();
				break;
			}
		}
		return super.onSpawn(npc);
	}
	
	private void attackRandomTarget(Npc npc)
	{
		final Collection<PlayerInstance> players = World.getInstance().getVisibleObjects(npc, PlayerInstance.class);
		{
			if ((players == null) || players.isEmpty())
			{
				return;
			}
			if (!players.isEmpty())
			{
				addAttackPlayerDesire(npc, players.stream().findAny().get());
			}
		}
	}
	
	public static void main(String[] args)
	{
		new Vortex();
	}
}
