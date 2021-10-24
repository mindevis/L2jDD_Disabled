
package ai.areas.BeastFarm;

import org.l2jdd.commons.util.CommonUtil;
import org.l2jdd.gameserver.ai.CtrlIntention;
import org.l2jdd.gameserver.model.actor.Npc;
import org.l2jdd.gameserver.model.actor.Summon;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.model.events.EventType;
import org.l2jdd.gameserver.model.events.ListenerRegisterType;
import org.l2jdd.gameserver.model.events.annotations.RegisterEvent;
import org.l2jdd.gameserver.model.events.annotations.RegisterType;
import org.l2jdd.gameserver.model.events.impl.creature.player.OnPlayerLogout;
import org.l2jdd.gameserver.model.holders.SkillHolder;
import org.l2jdd.gameserver.model.skills.SkillCaster;
import org.l2jdd.gameserver.network.SystemMessageId;
import org.l2jdd.gameserver.network.serverpackets.SystemMessage;

import ai.AbstractNpcAI;

/**
 * Baby Pets AI.
 * @author St3eT
 */
public class BabyPets extends AbstractNpcAI
{
	// NPCs
	private static final int[] BABY_PETS =
	{
		12780, // Baby Buffalo
		12781, // Baby Kookaburra
		12782, // Baby Cougar
	};
	// Skills
	private static final int HEAL_1 = 4717; // Heal Trick
	private static final int HEAL_2 = 4718; // Greater Heal Trick
	
	private BabyPets()
	{
		addSummonSpawnId(BABY_PETS);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, PlayerInstance player)
	{
		if (event.equals("HEAL") && (player != null))
		{
			final Summon summon = player.getPet();
			if (summon != null)
			{
				if (getRandom(100) <= 25)
				{
					castHeal(summon, new SkillHolder(HEAL_1, getHealLv(summon)), 80);
				}
				
				if (getRandom(100) <= 75)
				{
					castHeal(summon, new SkillHolder(HEAL_2, getHealLv(summon)), 15);
				}
			}
			else
			{
				cancelQuestTimer("HEAL", null, player);
			}
		}
		return super.onAdvEvent(event, npc, player);
	}
	
	@RegisterEvent(EventType.ON_PLAYER_LOGOUT)
	@RegisterType(ListenerRegisterType.GLOBAL)
	public void OnPlayerLogout(OnPlayerLogout event)
	{
		cancelQuestTimer("HEAL", null, event.getPlayer());
	}
	
	@Override
	public void onSummonSpawn(Summon summon)
	{
		startQuestTimer("HEAL", 5000, null, summon.getOwner(), true);
	}
	
	private int getHealLv(Summon summon)
	{
		final int summonLv = summon.getLevel();
		return CommonUtil.constrain(summonLv < 70 ? (summonLv / 10) : (7 + ((summonLv - 70) / 5)), 1, 12);
	}
	
	private void castHeal(Summon summon, SkillHolder skill, int maxHpPer)
	{
		final boolean previousFollowStatus = summon.getFollowStatus();
		final PlayerInstance owner = summon.getOwner();
		if (!owner.isDead() && (((owner.getCurrentHp() / owner.getMaxHp()) * 100) < maxHpPer) && !summon.isHungry() && SkillCaster.checkUseConditions(summon, skill.getSkill()))
		{
			summon.getAI().setIntention(CtrlIntention.AI_INTENTION_CAST, skill.getSkill(), owner);
			summon.sendPacket(new SystemMessage(SystemMessageId.YOUR_PET_USES_S1).addSkillName(skill.getSkill()));
			if (previousFollowStatus != summon.getFollowStatus())
			{
				summon.setFollowStatus(previousFollowStatus);
			}
		}
	}
	
	public static void main(String[] args)
	{
		new BabyPets();
	}
}