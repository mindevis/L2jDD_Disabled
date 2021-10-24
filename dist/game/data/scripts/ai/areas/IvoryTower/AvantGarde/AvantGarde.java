
package ai.areas.IvoryTower.AvantGarde;

import java.util.List;

import org.l2jdd.gameserver.data.xml.MultisellData;
import org.l2jdd.gameserver.data.xml.SkillTreeData;
import org.l2jdd.gameserver.enums.AcquireSkillType;
import org.l2jdd.gameserver.model.SkillLearn;
import org.l2jdd.gameserver.model.actor.Npc;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.model.skills.Skill;
import org.l2jdd.gameserver.network.SystemMessageId;
import org.l2jdd.gameserver.network.clientpackets.RequestAcquireSkill;
import org.l2jdd.gameserver.network.serverpackets.ExAcquirableSkillListByClass;
import org.l2jdd.gameserver.network.serverpackets.SystemMessage;

import ai.AbstractNpcAI;

/**
 * Avant-Garde AI.<br>
 * Transformation skill learning and transformation scroll sell.
 * @author Zoey76
 */
public class AvantGarde extends AbstractNpcAI
{
	// NPC
	private static final int AVANT_GARDE = 32323;
	
	public AvantGarde()
	{
		addStartNpc(AVANT_GARDE);
		addTalkId(AVANT_GARDE);
		addFirstTalkId(AVANT_GARDE);
		addAcquireSkillId(AVANT_GARDE);
	}
	
	@Override
	public String onAcquireSkill(Npc npc, PlayerInstance player, Skill skill, AcquireSkillType type)
	{
		if (type == AcquireSkillType.TRANSFORM)
		{
			showTransformSkillList(player);
		}
		return null;
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, PlayerInstance player)
	{
		String htmltext = null;
		switch (event)
		{
			case "32323-02.html":
			case "32323-02a.html":
			case "32323-02b.html":
			case "32323-02c.html":
			case "32323-05.html":
			case "32323-05no.html":
			case "32323-06.html":
			case "32323-06no.html":
			{
				htmltext = event;
				break;
			}
			case "LearnTransformationSkill":
			{
				if (RequestAcquireSkill.canTransform(player))
				{
					showTransformSkillList(player);
				}
				else
				{
					htmltext = "32323-03.html";
				}
				break;
			}
			case "BuyTransformationItems":
			{
				if (RequestAcquireSkill.canTransform(player))
				{
					MultisellData.getInstance().separateAndSend(32323001, player, npc, false);
				}
				else
				{
					htmltext = "32323-04.html";
				}
				break;
			}
		}
		return htmltext;
	}
	
	@Override
	public String onFirstTalk(Npc npc, PlayerInstance player)
	{
		return "32323-01.html";
	}
	
	@Override
	public String onTalk(Npc npc, PlayerInstance talker)
	{
		return "32323-01.html";
	}
	
	/**
	 * This displays Transformation Skill List to the player.
	 * @param player the active character.
	 */
	public static void showTransformSkillList(PlayerInstance player)
	{
		final List<SkillLearn> skills = SkillTreeData.getInstance().getAvailableTransformSkills(player);
		if (skills.isEmpty())
		{
			final int minlevel = SkillTreeData.getInstance().getMinLevelForNewSkill(player, SkillTreeData.getInstance().getTransformSkillTree());
			if (minlevel > 0)
			{
				// No more skills to learn, come back when you level.
				final SystemMessage sm = new SystemMessage(SystemMessageId.YOU_DO_NOT_HAVE_ANY_FURTHER_SKILLS_TO_LEARN_COME_BACK_WHEN_YOU_HAVE_REACHED_LEVEL_S1);
				sm.addInt(minlevel);
				player.sendPacket(sm);
			}
			else
			{
				player.sendPacket(SystemMessageId.THERE_ARE_NO_OTHER_SKILLS_TO_LEARN);
			}
		}
		else
		{
			player.sendPacket(new ExAcquirableSkillListByClass(skills, AcquireSkillType.TRANSFORM));
		}
	}
	
	public static void main(String[] args)
	{
		new AvantGarde();
	}
}
