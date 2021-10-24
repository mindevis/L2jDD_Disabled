
package ai.others.HealerTrainer;

import java.util.Collection;
import java.util.List;

import org.l2jdd.Config;
import org.l2jdd.gameserver.data.xml.SkillTreeData;
import org.l2jdd.gameserver.enums.AcquireSkillType;
import org.l2jdd.gameserver.enums.CategoryType;
import org.l2jdd.gameserver.model.SkillLearn;
import org.l2jdd.gameserver.model.actor.Npc;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.model.holders.ItemHolder;
import org.l2jdd.gameserver.model.skills.Skill;
import org.l2jdd.gameserver.network.SystemMessageId;
import org.l2jdd.gameserver.network.serverpackets.ExAcquirableSkillListByClass;

import ai.AbstractNpcAI;

/**
 * Trainer healers AI.
 * @author Zoey76
 */
public class HealerTrainer extends AbstractNpcAI
{
	// NPC
	// @formatter:off
	private static final int[] HEALER_TRAINERS =
	{
		30022, 30032, 30036, 30067, 30068, 30116, 30117, 30118, 30119,
		30144, 30145, 30188, 30194, 30375, 30377, 30464, 30473,
		30476, 30680, 30701, 30858, 30859, 30860, 30861, 30864,30906, 30908, 
		30912, 31280, 31281, 31287, 31329, 31330, 31335, 31969, 31970, 31976, 
		32155, 32162, 32161, 32156, 32148
	};
	// @formatter:on
	// Misc
	private static final int MIN_LEVEL = 76;
	private static final CategoryType[] ALLOWED_CATEGORIES =
	{
		CategoryType.FOURTH_CLASS_GROUP,
		CategoryType.FIFTH_CLASS_GROUP,
		CategoryType.SIXTH_CLASS_GROUP
	};
	
	private HealerTrainer()
	{
		addStartNpc(HEALER_TRAINERS);
		addTalkId(HEALER_TRAINERS);
		addFirstTalkId(HEALER_TRAINERS);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, PlayerInstance player)
	{
		String htmltext = null;
		switch (event)
		{
			case "30864.html":
			case "30864-1.html":
			{
				htmltext = event;
				break;
			}
			case "SkillTransfer":
			{
				htmltext = "main.html";
				break;
			}
			case "SkillTransferLearn":
			{
				if (!player.isInCategory(CategoryType.HEAL_MASTER))
				{
					htmltext = npc.getId() + "-noteach.html";
					break;
				}
				
				if ((player.getLevel() < MIN_LEVEL) || !player.isInOneOfCategory(ALLOWED_CATEGORIES))
				{
					htmltext = "learn-lowlevel.html";
					break;
				}
				
				final List<SkillLearn> skills = SkillTreeData.getInstance().getAvailableTransferSkills(player);
				if (skills.isEmpty())
				{
					player.sendPacket(SystemMessageId.THERE_ARE_NO_OTHER_SKILLS_TO_LEARN);
				}
				else
				{
					player.sendPacket(new ExAcquirableSkillListByClass(skills, AcquireSkillType.TRANSFER));
				}
				break;
			}
			case "SkillTransferCleanse":
			{
				if (!player.isInCategory(CategoryType.HEAL_MASTER))
				{
					htmltext = "cleanse-no.html";
					break;
				}
				
				if ((player.getLevel() < MIN_LEVEL) || !player.isInOneOfCategory(ALLOWED_CATEGORIES))
				{
					htmltext = "cleanse-no.html";
					break;
				}
				
				if (player.getAdena() < Config.FEE_DELETE_TRANSFER_SKILLS)
				{
					player.sendPacket(SystemMessageId.YOU_CANNOT_RESET_THE_SKILL_LINK_BECAUSE_THERE_IS_NOT_ENOUGH_ADENA);
					break;
				}
				
				if (hasTransferSkillItems(player))
				{
					// Come back when you have used all transfer skill items for this class.
					htmltext = "cleanse-no_skills.html";
				}
				else
				{
					boolean hasSkills = false;
					final Collection<SkillLearn> skills = SkillTreeData.getInstance().getTransferSkillTree(player.getClassId()).values();
					for (SkillLearn skillLearn : skills)
					{
						final Skill skill = player.getKnownSkill(skillLearn.getSkillId());
						if (skill != null)
						{
							player.removeSkill(skill);
							for (ItemHolder item : skillLearn.getRequiredItems())
							{
								player.addItem("Cleanse", item.getId(), item.getCount(), npc, true);
							}
							hasSkills = true;
						}
					}
					
					// Adena gets reduced once.
					if (hasSkills)
					{
						player.reduceAdena("Cleanse", Config.FEE_DELETE_TRANSFER_SKILLS, npc, true);
					}
				}
				break;
			}
		}
		return htmltext;
	}
	
	/**
	 * Verify if the player has the required item.
	 * @param player the player to verify
	 * @return {@code true} if the player has the item for the current class, {@code false} otherwise
	 */
	private static boolean hasTransferSkillItems(PlayerInstance player)
	{
		int itemId;
		switch (player.getClassId())
		{
			case CARDINAL:
			{
				itemId = 15307;
				break;
			}
			case EVA_SAINT:
			{
				itemId = 15308;
				break;
			}
			case SHILLIEN_SAINT:
			{
				itemId = 15309;
				break;
			}
			default:
			{
				itemId = -1;
			}
		}
		return (player.getInventory().getInventoryItemCount(itemId, -1) > 0);
	}
	
	public static void main(String[] args)
	{
		new HealerTrainer();
	}
}
