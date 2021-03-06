
package ai.bosses.Freya.IceQueensCastle;

import org.l2jdd.gameserver.ai.CtrlIntention;
import org.l2jdd.gameserver.enums.ChatType;
import org.l2jdd.gameserver.enums.Movie;
import org.l2jdd.gameserver.model.Location;
import org.l2jdd.gameserver.model.World;
import org.l2jdd.gameserver.model.actor.Attackable;
import org.l2jdd.gameserver.model.actor.Creature;
import org.l2jdd.gameserver.model.actor.Npc;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.model.holders.SkillHolder;
import org.l2jdd.gameserver.model.instancezone.Instance;
import org.l2jdd.gameserver.model.skills.Skill;
import org.l2jdd.gameserver.network.NpcStringId;

import instances.AbstractInstance;

/**
 * Ice Queen's Castle instance zone.
 * @author Adry_85
 */
public class IceQueensCastle extends AbstractInstance
{
	// NPCs
	private static final int FREYA = 18847;
	private static final int BATTALION_LEADER = 18848;
	private static final int LEGIONNAIRE = 18849;
	private static final int MERCENARY_ARCHER = 18926;
	private static final int ARCHERY_KNIGHT = 22767;
	private static final int JINIA = 32781;
	// Locations
	private static final Location FREYA_LOC = new Location(114730, -114805, -11200, 50);
	// Skill
	private static SkillHolder ETHERNAL_BLIZZARD = new SkillHolder(6276, 1);
	// Misc
	private static final int TEMPLATE_ID = 137;
	
	public IceQueensCastle()
	{
		super(TEMPLATE_ID);
		addStartNpc(JINIA);
		addTalkId(JINIA);
		addSeeCreatureId(BATTALION_LEADER, LEGIONNAIRE, MERCENARY_ARCHER);
		addSpawnId(FREYA);
		addSpellFinishedId(FREYA);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, PlayerInstance player)
	{
		switch (event)
		{
			case "ATTACK_KNIGHT":
			{
				World.getInstance().forEachVisibleObject(npc, Npc.class, mob ->
				{
					if ((mob.getId() == ARCHERY_KNIGHT) && !mob.isDead() && !mob.isDecayed())
					{
						npc.setRunning();
						npc.getAI().setIntention(CtrlIntention.AI_INTENTION_ATTACK, mob);
						((Attackable) npc).addDamageHate(mob, 0, 999999);
					}
				});
				startQuestTimer("ATTACK_KNIGHT", 3000, npc, null);
				break;
			}
			case "TIMER_MOVING":
			{
				if (npc != null)
				{
					npc.getAI().setIntention(CtrlIntention.AI_INTENTION_MOVE_TO, FREYA_LOC);
				}
				break;
			}
			case "TIMER_BLIZZARD":
			{
				npc.broadcastSay(ChatType.NPC_GENERAL, NpcStringId.I_CAN_NO_LONGER_STAND_BY);
				npc.stopMove(null);
				npc.setTarget(player);
				npc.doCast(ETHERNAL_BLIZZARD.getSkill());
				break;
			}
			case "TIMER_SCENE_21":
			{
				if (npc != null)
				{
					playMovie(player, Movie.SC_BOSS_FREYA_FORCED_DEFEAT);
					startQuestTimer("TIMER_PC_LEAVE", 24000, null, player);
					npc.deleteMe();
				}
				break;
			}
			case "TIMER_PC_LEAVE":
			{
				finishInstance(player, 0);
				break;
			}
		}
		return super.onAdvEvent(event, npc, player);
	}
	
	@Override
	public String onTalk(Npc npc, PlayerInstance talker)
	{
		enterInstance(talker, npc, TEMPLATE_ID);
		return super.onTalk(npc, talker);
	}
	
	@Override
	public String onSpawn(Npc npc)
	{
		startQuestTimer("TIMER_MOVING", 60000, npc, null);
		startQuestTimer("TIMER_BLIZZARD", 180000, npc, null);
		return super.onSpawn(npc);
	}
	
	@Override
	public String onSeeCreature(Npc npc, Creature creature, boolean isSummon)
	{
		if (creature.isPlayer() && npc.isScriptValue(0))
		{
			npc.setScriptValue(1);
			npc.broadcastSay(ChatType.NPC_GENERAL, NpcStringId.S1_MAY_THE_PROTECTION_OF_THE_GODS_BE_UPON_YOU, creature.getName());
			World.getInstance().forEachVisibleObject(npc, Npc.class, mob ->
			{
				if ((mob.getId() == ARCHERY_KNIGHT) && !mob.isDead() && !mob.isDecayed())
				{
					npc.setRunning();
					npc.getAI().setIntention(CtrlIntention.AI_INTENTION_ATTACK, mob);
					((Attackable) npc).addDamageHate(mob, 0, 999999);
				}
			});
			startQuestTimer("ATTACK_KNIGHT", 5000, npc, null);
		}
		return super.onSeeCreature(npc, creature, isSummon);
	}
	
	@Override
	public String onSpellFinished(Npc npc, PlayerInstance player, Skill skill)
	{
		final Instance world = npc.getInstanceWorld();
		if ((world != null) && (skill == ETHERNAL_BLIZZARD.getSkill()))
		{
			final PlayerInstance playerInside = world.getFirstPlayer();
			if (playerInside != null)
			{
				startQuestTimer("TIMER_SCENE_21", 1000, npc, playerInside);
			}
		}
		return super.onSpellFinished(npc, player, skill);
	}
	
	public static void main(String[] args)
	{
		new IceQueensCastle();
	}
}