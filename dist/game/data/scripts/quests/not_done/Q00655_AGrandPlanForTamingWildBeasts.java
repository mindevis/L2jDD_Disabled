
package quests.not_done;

import org.l2jdd.Config;
import org.l2jdd.gameserver.model.quest.Quest;

/**
 * @author Mobius
 */
public class Q00655_AGrandPlanForTamingWildBeasts extends Quest
{
	private static final int START_NPC = 35627;
	
	public Q00655_AGrandPlanForTamingWildBeasts()
	{
		super(655);
		addStartNpc(START_NPC);
		addTalkId(START_NPC);
		addCondMinLevel(Config.PLAYER_MAXIMUM_LEVEL, getNoQuestMsg(null));
	}
}
