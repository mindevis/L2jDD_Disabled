
package ai.others.AdventurersGuide;

import org.l2jdd.gameserver.model.actor.Npc;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.model.holders.SkillHolder;
import org.l2jdd.gameserver.model.skills.Skill;
import org.l2jdd.gameserver.model.skills.SkillCaster;

import ai.AbstractNpcAI;

/**
 * Adventurers Guide AI.
 * @author St3eT
 */
public class AdventurersGuide extends AbstractNpcAI
{
	// NPC
	private static final int[] ADVENTURERS_GUIDE =
	{
		32327,
		33950,
	};
	// Items
	private static final int ADENA = 57;
	private static final int GEMSTONE_R = 19440;
	// Skills
	private static final SkillHolder BLESS_PROTECTION = new SkillHolder(5182, 1); // Blessing of Protection
	private static final SkillHolder FANTASIA = new SkillHolder(32840, 1); // Fantasia Harmony - Adventurer
	private static final SkillHolder[] GROUP_BUFFS =
	{
		new SkillHolder(15642, 1), // Horn Melody (Adventurer)
		new SkillHolder(15643, 1), // Drum Melody (Adventurer)
		new SkillHolder(15644, 1), // Pipe Organ Melody (Adventurer)
		new SkillHolder(15645, 1), // Guitar Melody (Adventurer)
		new SkillHolder(15651, 1), // Prevailing Sonata (Adventurer)
		new SkillHolder(15652, 1), // Daring Sonata (Adventurer)
		new SkillHolder(15653, 1), // Refreshing Sonata (Adventurer)
	};
	private static final SkillHolder[] DONATE_BUFFS =
	{
		new SkillHolder(15642, 4), // Horn Melody (Adventurer)
		new SkillHolder(15643, 4), // Drum Melody (Adventurer)
		new SkillHolder(15644, 4), // Pipe Organ Melody (Adventurer)
		new SkillHolder(15645, 4), // Guitar Melody (Adventurer)
		new SkillHolder(15651, 1), // Prevailing Sonata (Adventurer)
		new SkillHolder(15652, 1), // Daring Sonata (Adventurer)
		new SkillHolder(15653, 1), // Refreshing Sonata (Adventurer)
	};
	// Misc
	private static int MAX_LEVEL_BUFFS = 99;
	private static int MIN_LEVEL_PROTECTION = 40;
	
	private AdventurersGuide()
	{
		addStartNpc(ADVENTURERS_GUIDE);
		addTalkId(ADVENTURERS_GUIDE);
		addFirstTalkId(ADVENTURERS_GUIDE);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, PlayerInstance player)
	{
		String htmltext = null;
		switch (event)
		{
			case "guide-01.html":
			case "guide-02.html":
			case "guide-03.html":
			case "guide-04.html":
			case "guide-05.html":
			case "guide-06.html":
			case "guide-07.html":
			case "guide-08.html":
			{
				htmltext = event;
				break;
			}
			case "index":
			{
				htmltext = npc.getId() + ".html";
				break;
			}
			case "weakenBreath":
			{
				if (player.getShilensBreathDebuffLevel() < 3)
				{
					htmltext = "guide-noBreath.html";
					break;
				}
				player.setShilensBreathDebuffLevel(2);
				htmltext = "guide-cleanedBreath.html";
				break;
			}
			case "fantasia":
			{
				if (player.getLevel() > MAX_LEVEL_BUFFS)
				{
					return "guide-noBuffs.html";
				}
				for (SkillHolder holder : GROUP_BUFFS)
				{
					SkillCaster.triggerCast(npc, player, holder.getSkill());
				}
				htmltext = applyBuffs(npc, player, FANTASIA.getSkill());
				break;
			}
			case "fantasia_donate_adena":
			{
				if (getQuestItemsCount(player, ADENA) >= 3000000)
				{
					takeItems(player, ADENA, 3000000);
					for (SkillHolder holder : DONATE_BUFFS)
					{
						SkillCaster.triggerCast(npc, player, holder.getSkill());
					}
					htmltext = applyBuffs(npc, player, FANTASIA.getSkill());
				}
				else
				{
					htmltext = "guide-noItems.html";
				}
				break;
			}
			case "fantasia_donate_gemstones":
			{
				if (getQuestItemsCount(player, GEMSTONE_R) >= 5)
				{
					takeItems(player, GEMSTONE_R, 5);
					for (SkillHolder holder : DONATE_BUFFS)
					{
						SkillCaster.triggerCast(npc, player, holder.getSkill());
					}
					htmltext = applyBuffs(npc, player, FANTASIA.getSkill());
				}
				else
				{
					htmltext = "guide-noItems.html";
				}
				break;
			}
		}
		return htmltext;
	}
	
	private String applyBuffs(Npc npc, PlayerInstance player, Skill skill)
	{
		for (SkillHolder holder : GROUP_BUFFS)
		{
			SkillCaster.triggerCast(npc, player, holder.getSkill());
		}
		SkillCaster.triggerCast(npc, player, skill);
		if ((player.getLevel() < MIN_LEVEL_PROTECTION) && (player.getClassId().level() <= 1))
		{
			SkillCaster.triggerCast(npc, player, BLESS_PROTECTION.getSkill());
		}
		return null;
	}
	
	public static void main(String[] args)
	{
		new AdventurersGuide();
	}
}