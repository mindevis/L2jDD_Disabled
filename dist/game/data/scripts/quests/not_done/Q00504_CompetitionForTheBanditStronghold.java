
package quests.not_done;

import org.l2jdd.Config;
import org.l2jdd.gameserver.model.quest.Quest;

/**
 * @author Mobius
 */
public class Q00504_CompetitionForTheBanditStronghold extends Quest
{
	private static final int START_NPC = 35437;
	
	public Q00504_CompetitionForTheBanditStronghold()
	{
		super(504);
		addStartNpc(START_NPC);
		addTalkId(START_NPC);
		addCondMinLevel(Config.PLAYER_MAXIMUM_LEVEL, getNoQuestMsg(null));
	}
}
