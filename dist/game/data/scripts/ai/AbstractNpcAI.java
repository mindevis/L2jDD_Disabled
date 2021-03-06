
package ai;

import java.util.logging.Logger;

import org.l2jdd.gameserver.model.Location;
import org.l2jdd.gameserver.model.World;
import org.l2jdd.gameserver.model.actor.Npc;
import org.l2jdd.gameserver.model.actor.instance.MonsterInstance;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.model.holders.MinionHolder;
import org.l2jdd.gameserver.model.quest.Quest;
import org.l2jdd.gameserver.util.Util;

/**
 * Abstract NPC AI class for datapack based AIs.
 * @author UnAfraid, Zoey76
 */
public abstract class AbstractNpcAI extends Quest
{
	protected final Logger LOGGER = Logger.getLogger(getClass().getName());
	
	public AbstractNpcAI()
	{
		super(-1);
	}
	
	/**
	 * Simple on first talk event handler.
	 */
	@Override
	public String onFirstTalk(Npc npc, PlayerInstance player)
	{
		return npc.getId() + ".html";
	}
	
	/**
	 * Registers the following events to the current script:<br>
	 * <ul>
	 * <li>ON_ATTACK</li>
	 * <li>ON_KILL</li>
	 * <li>ON_SPAWN</li>
	 * <li>ON_SPELL_FINISHED</li>
	 * <li>ON_SKILL_SEE</li>
	 * <li>ON_FACTION_CALL</li>
	 * <li>ON_AGGR_RANGE_ENTER</li>
	 * </ul>
	 * @param mobs
	 */
	public void registerMobs(int... mobs)
	{
		addAttackId(mobs);
		addKillId(mobs);
		addSpawnId(mobs);
		addSpellFinishedId(mobs);
		addSkillSeeId(mobs);
		addAggroRangeEnterId(mobs);
		addFactionCallId(mobs);
	}
	
	public void spawnMinions(Npc npc, String spawnName)
	{
		for (MinionHolder is : npc.getParameters().getMinionList(spawnName))
		{
			addMinion((MonsterInstance) npc, is.getId());
		}
	}
	
	protected void followNpc(Npc npc, int followedNpcId, int followingAngle, int minDistance, int maxDistance)
	{
		World.getInstance().forEachVisibleObject(npc, Npc.class, npcAround ->
		{
			if (npcAround.getId() != followedNpcId)
			{
				return;
			}
			
			final double distance = npc.calculateDistance3D(npcAround);
			if ((distance >= maxDistance) && npc.isScriptValue(0))
			{
				npc.setRunning();
				npc.setScriptValue(1);
			}
			else if ((distance <= (minDistance * 1.5)) && npc.isScriptValue(1))
			{
				npc.setWalking();
				npc.setScriptValue(0);
			}
			
			final double course = Math.toRadians(followingAngle);
			final double radian = Math.toRadians(Util.convertHeadingToDegree(npcAround.getHeading()));
			final double nRadius = npc.getCollisionRadius() + npcAround.getCollisionRadius() + minDistance;
			final int x = npcAround.getLocation().getX() + (int) (Math.cos(Math.PI + radian + course) * nRadius);
			final int y = npcAround.getLocation().getY() + (int) (Math.sin(Math.PI + radian + course) * nRadius);
			final int z = npcAround.getLocation().getZ();
			npc.getAI().moveTo(new Location(x, y, z));
		});
	}
}