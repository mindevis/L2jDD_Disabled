
package ai.others.AlchemistManager;

import java.util.List;

import org.l2jdd.gameserver.data.xml.SkillTreeData;
import org.l2jdd.gameserver.enums.AcquireSkillType;
import org.l2jdd.gameserver.enums.Race;
import org.l2jdd.gameserver.model.SkillLearn;
import org.l2jdd.gameserver.model.actor.Npc;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.network.SystemMessageId;
import org.l2jdd.gameserver.network.serverpackets.ExAcquirableSkillListByClass;
import org.l2jdd.gameserver.network.serverpackets.ExTutorialShowId;

import ai.AbstractNpcAI;

/**
 * Alchemist Manager AI.
 * @author Sdw
 */
public class AlchemistManager extends AbstractNpcAI
{
	// NPCs
	private static final int[] ALCHEMISTS =
	{
		33978, // Zephyra
		33977, // Veruti
	};
	
	private AlchemistManager()
	{
		addStartNpc(ALCHEMISTS);
		addTalkId(ALCHEMISTS);
		addFirstTalkId(ALCHEMISTS);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, PlayerInstance player)
	{
		String htmltext = null;
		switch (event)
		{
			case "33978.html":
			case "33977.html":
			{
				htmltext = event;
				break;
			}
			case "open_tutorial":
			{
				player.sendPacket(new ExTutorialShowId(26));
				htmltext = npc.getId() + "-1.html";
				break;
			}
			case "learn_skill":
			{
				if (player.getRace() == Race.ERTHEIA)
				{
					final List<SkillLearn> alchemySkills = SkillTreeData.getInstance().getAvailableAlchemySkills(player);
					if (alchemySkills.isEmpty())
					{
						player.sendPacket(SystemMessageId.THERE_ARE_NO_OTHER_SKILLS_TO_LEARN);
					}
					else
					{
						player.sendPacket(new ExAcquirableSkillListByClass(alchemySkills, AcquireSkillType.ALCHEMY));
					}
				}
				else
				{
					htmltext = npc.getId() + "-2.html";
				}
				break;
			}
		}
		return htmltext;
	}
	
	public static void main(String[] args)
	{
		new AlchemistManager();
	}
}