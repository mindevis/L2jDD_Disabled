
package quests.not_done;

import org.l2jdd.Config;
import org.l2jdd.gameserver.model.quest.Quest;

/**
 * @author Mobius
 */
public class Q00839_RequestFromTheUnworldlyVisitors extends Quest
{
	private static final int START_NPC = 36753;
	
	public Q00839_RequestFromTheUnworldlyVisitors()
	{
		super(839);
		addStartNpc(START_NPC);
		addTalkId(START_NPC);
		addCondMinLevel(Config.PLAYER_MAXIMUM_LEVEL, getNoQuestMsg(null));
	}
}
