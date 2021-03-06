
package ai.bosses.Frintezza;

import static org.l2jdd.gameserver.ai.CtrlIntention.AI_INTENTION_ATTACK;
import static org.l2jdd.gameserver.ai.CtrlIntention.AI_INTENTION_FOLLOW;
import static org.l2jdd.gameserver.ai.CtrlIntention.AI_INTENTION_IDLE;

import java.util.ArrayList;
import java.util.List;

import org.l2jdd.commons.util.Chronos;
import org.l2jdd.gameserver.data.xml.SkillData;
import org.l2jdd.gameserver.geoengine.GeoEngine;
import org.l2jdd.gameserver.model.actor.Creature;
import org.l2jdd.gameserver.model.actor.Npc;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.model.skills.Skill;
import org.l2jdd.gameserver.util.Util;

import ai.AbstractNpcAI;

/**
 * @author Micr0, Zerox, Mobius
 */
public class ScarletVanHalisha extends AbstractNpcAI
{
	// NPCs
	private static final int HALISHA2 = 29046;
	private static final int HALISHA3 = 29047;
	// Skills
	private static final int FRINTEZZA_DAEMON_ATTACK = 5014;
	private static final int FRINTEZZA_DAEMON_CHARGE = 5015;
	private static final int YOKE_OF_SCARLET = 5016;
	private static final int FRINTEZZA_DAEMON_MORPH = 5018;
	private static final int FRINTEZZA_DAEMON_FIELD = 5019;
	// Misc
	private static final int RANGED_SKILL_MIN_COOLTIME = 60000; // 1 minute
	private Creature _target;
	private Skill _skill;
	private long _lastRangedSkillTime;
	
	public ScarletVanHalisha()
	{
		addAttackId(HALISHA2, HALISHA3);
		addKillId(HALISHA2, HALISHA3);
		addSpellFinishedId(HALISHA2, HALISHA3);
		registerMobs(HALISHA2, HALISHA3);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, PlayerInstance player)
	{
		switch (event)
		{
			case "attack":
			{
				if (npc != null)
				{
					getSkillAI(npc);
				}
				break;
			}
			case "random_target":
			{
				_target = getRandomTarget(npc, null);
				break;
			}
		}
		return super.onAdvEvent(event, npc, player);
	}
	
	@Override
	public String onSpellFinished(Npc npc, PlayerInstance player, Skill skill)
	{
		getSkillAI(npc);
		return super.onSpellFinished(npc, player, skill);
	}
	
	@Override
	public String onAttack(Npc npc, PlayerInstance attacker, int damage, boolean isSummon)
	{
		startQuestTimer("random_Target", 5000, npc, null, true);
		startQuestTimer("attack", 500, npc, null, true);
		return super.onAttack(npc, attacker, damage, isSummon);
	}
	
	@Override
	public String onKill(Npc npc, PlayerInstance killer, boolean isSummon)
	{
		cancelQuestTimer("attack", npc, null);
		cancelQuestTimer("random_Target", npc, null);
		return super.onKill(npc, killer, isSummon);
	}
	
	private Skill getRndSkills(Npc npc)
	{
		switch (npc.getId())
		{
			case HALISHA2:
			{
				if (getRandom(100) < 10)
				{
					return SkillData.getInstance().getSkill(FRINTEZZA_DAEMON_CHARGE, 2);
				}
				else if (getRandom(100) < 10)
				{
					return SkillData.getInstance().getSkill(FRINTEZZA_DAEMON_CHARGE, 5);
				}
				else if (getRandom(100) < 2)
				{
					return SkillData.getInstance().getSkill(YOKE_OF_SCARLET, 1);
				}
				else
				{
					return SkillData.getInstance().getSkill(FRINTEZZA_DAEMON_ATTACK, 2);
				}
			}
			case HALISHA3:
			{
				if (getRandom(100) < 10)
				{
					return SkillData.getInstance().getSkill(FRINTEZZA_DAEMON_CHARGE, 3);
				}
				else if (getRandom(100) < 10)
				{
					return SkillData.getInstance().getSkill(FRINTEZZA_DAEMON_CHARGE, 6);
				}
				else if (getRandom(100) < 10)
				{
					return SkillData.getInstance().getSkill(FRINTEZZA_DAEMON_CHARGE, 2);
				}
				else if (((_lastRangedSkillTime + RANGED_SKILL_MIN_COOLTIME) < Chronos.currentTimeMillis()) && (getRandom(100) < 10))
				{
					return SkillData.getInstance().getSkill(FRINTEZZA_DAEMON_FIELD, 1);
				}
				else if (((_lastRangedSkillTime + RANGED_SKILL_MIN_COOLTIME) < Chronos.currentTimeMillis()) && (getRandom(100) < 10))
				{
					return SkillData.getInstance().getSkill(FRINTEZZA_DAEMON_MORPH, 1);
				}
				else if (getRandom(100) < 2)
				{
					return SkillData.getInstance().getSkill(YOKE_OF_SCARLET, 1);
				}
				else
				{
					return SkillData.getInstance().getSkill(FRINTEZZA_DAEMON_ATTACK, 3);
				}
			}
		}
		return SkillData.getInstance().getSkill(FRINTEZZA_DAEMON_ATTACK, 1);
	}
	
	private synchronized void getSkillAI(Npc npc)
	{
		if (npc.isInvul() || npc.isCastingNow())
		{
			return;
		}
		if ((getRandom(100) < 30) || (_target == null) || _target.isDead())
		{
			_skill = getRndSkills(npc);
			_target = getRandomTarget(npc, _skill);
		}
		final Creature target = _target;
		Skill skill = _skill;
		if (skill == null)
		{
			skill = getRndSkills(npc);
		}
		
		if (npc.isPhysicalMuted())
		{
			return;
		}
		
		if ((target == null) || target.isDead())
		{
			// npc.setCastingNow(false);
			return;
		}
		
		if (Util.checkIfInRange(skill.getCastRange(), npc, target, true))
		{
			npc.getAI().setIntention(AI_INTENTION_IDLE);
			npc.setTarget(target);
			// npc.setCastingNow(true);
			_target = null;
			npc.doCast(skill);
		}
		else
		{
			npc.getAI().setIntention(AI_INTENTION_FOLLOW, target, null);
			npc.getAI().setIntention(AI_INTENTION_ATTACK, target, null);
			// npc.setCastingNow(false);
		}
	}
	
	private Creature getRandomTarget(Npc npc, Skill skill)
	{
		final List<Creature> result = new ArrayList<>();
		{
			for (PlayerInstance obj : npc.getInstanceWorld().getPlayers())
			{
				if (obj.isPlayer() && obj.getActingPlayer().isInvisible())
				{
					continue;
				}
				
				if (((obj.getZ() < (npc.getZ() - 100)) && (obj.getZ() > (npc.getZ() + 100))) || !GeoEngine.getInstance().canSeeTarget(obj, npc))
				{
					continue;
				}
				
				int skillRange = 150;
				if (skill != null)
				{
					switch (skill.getId())
					{
						case FRINTEZZA_DAEMON_ATTACK:
						{
							skillRange = 150;
							break;
						}
						case FRINTEZZA_DAEMON_CHARGE:
						{
							skillRange = 400;
							break;
						}
						case YOKE_OF_SCARLET:
						{
							skillRange = 200;
							break;
						}
						case FRINTEZZA_DAEMON_MORPH:
						case FRINTEZZA_DAEMON_FIELD:
						{
							_lastRangedSkillTime = Chronos.currentTimeMillis();
							skillRange = 550;
							break;
						}
					}
					
					if (Util.checkIfInRange(skillRange, npc, obj, true) && !((Creature) obj).isDead())
					{
						result.add(obj);
					}
				}
			}
		}
		return getRandomEntry(result);
	}
	
	public static void main(String[] args)
	{
		new ScarletVanHalisha();
	}
}