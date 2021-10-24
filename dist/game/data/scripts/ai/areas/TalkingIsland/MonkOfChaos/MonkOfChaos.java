
package ai.areas.TalkingIsland.MonkOfChaos;

import java.util.List;

import org.l2jdd.gameserver.data.xml.SkillTreeData;
import org.l2jdd.gameserver.enums.AcquireSkillType;
import org.l2jdd.gameserver.enums.CategoryType;
import org.l2jdd.gameserver.enums.SubclassType;
import org.l2jdd.gameserver.model.SkillLearn;
import org.l2jdd.gameserver.model.actor.Npc;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.model.skills.Skill;
import org.l2jdd.gameserver.model.variables.PlayerVariables;
import org.l2jdd.gameserver.network.SystemMessageId;
import org.l2jdd.gameserver.network.serverpackets.ExAcquirableSkillListByClass;

import ai.AbstractNpcAI;

/**
 * Monk of Chaos AI.
 * @author Sdw
 * @author Mobius
 */
public class MonkOfChaos extends AbstractNpcAI
{
	private static final int MONK_OF_CHAOS = 33880;
	private static final int MIN_LEVEL = 85;
	private static final int CANCEL_FEE = 100000000;
	private static final int CHAOS_POMANDER = 37374;
	private static final int CHAOS_POMANDER_DUALCLASS = 37375;
	private static final String[] REVELATION_VAR_NAMES =
	{
		PlayerVariables.REVELATION_SKILL_1_MAIN_CLASS,
		PlayerVariables.REVELATION_SKILL_2_MAIN_CLASS,
	};
	
	private static final String[] DUALCLASS_REVELATION_VAR_NAMES =
	{
		PlayerVariables.REVELATION_SKILL_1_DUAL_CLASS,
		PlayerVariables.REVELATION_SKILL_2_DUAL_CLASS
	};
	
	private MonkOfChaos()
	{
		addStartNpc(MONK_OF_CHAOS);
		addTalkId(MONK_OF_CHAOS);
		addFirstTalkId(MONK_OF_CHAOS);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, PlayerInstance player)
	{
		String htmltext = null;
		switch (event)
		{
			case "33880-1.html":
			case "33880-2.html":
			{
				htmltext = event;
				break;
			}
			case "LearnRevelationSkills":
			{
				if ((player.getLevel() < MIN_LEVEL) || !player.isInCategory(CategoryType.SIXTH_CLASS_GROUP))
				{
					htmltext = "no-learn.html";
					break;
				}
				
				if (player.isSubClassActive() && !player.isDualClassActive())
				{
					htmltext = "no-subclass.html";
					break;
				}
				
				if (player.isDualClassActive())
				{
					final List<SkillLearn> skills = SkillTreeData.getInstance().getAvailableRevelationSkills(player, SubclassType.DUALCLASS);
					if (!skills.isEmpty())
					{
						player.sendPacket(new ExAcquirableSkillListByClass(skills, AcquireSkillType.REVELATION_DUALCLASS));
					}
					else
					{
						player.sendPacket(SystemMessageId.THERE_ARE_NO_OTHER_SKILLS_TO_LEARN);
					}
				}
				else
				{
					final List<SkillLearn> skills = SkillTreeData.getInstance().getAvailableRevelationSkills(player, SubclassType.BASECLASS);
					if (!skills.isEmpty())
					{
						player.sendPacket(new ExAcquirableSkillListByClass(skills, AcquireSkillType.REVELATION));
					}
					else
					{
						player.sendPacket(SystemMessageId.THERE_ARE_NO_OTHER_SKILLS_TO_LEARN);
					}
				}
				break;
			}
			case "CancelRevelationSkills":
			{
				if (player.isSubClassActive() && !player.isDualClassActive())
				{
					htmltext = "no-subclass.html";
					break;
				}
				
				final String[] varNames = player.isDualClassActive() ? DUALCLASS_REVELATION_VAR_NAMES : REVELATION_VAR_NAMES;
				int count = 0;
				for (String varName : varNames)
				{
					if (player.getVariables().getInt(varName, 0) > 0)
					{
						count++;
					}
				}
				if ((player.getLevel() < MIN_LEVEL) || !player.isInCategory(CategoryType.SIXTH_CLASS_GROUP) || (count == 0))
				{
					htmltext = "no-cancel.html";
					break;
				}
				
				if (player.getAdena() < CANCEL_FEE)
				{
					htmltext = "no-adena.html";
					break;
				}
				takeItems(player, 57, CANCEL_FEE);
				
				for (SkillLearn skillLearn : SkillTreeData.getInstance().getAllRevelationSkills(player, player.isDualClassActive() ? SubclassType.DUALCLASS : SubclassType.BASECLASS))
				{
					final Skill skill = player.getKnownSkill(skillLearn.getSkillId());
					if (skill != null)
					{
						player.removeSkill(skill);
					}
				}
				for (String varName : varNames)
				{
					player.getVariables().remove(varName);
				}
				
				giveItems(player, player.isDualClassActive() ? CHAOS_POMANDER_DUALCLASS : CHAOS_POMANDER, count);
				htmltext = "canceled.html";
				break;
			}
		}
		return htmltext;
	}
	
	public static void main(String[] args)
	{
		new MonkOfChaos();
	}
}