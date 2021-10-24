
package quests.not_done;

import org.l2jdd.Config;
import org.l2jdd.gameserver.model.quest.Quest;

/**
 * @author Mobius
 */
public class Q00836_RequestFromTheBlackbirdClan extends Quest
{
	private static final int START_NPC = 36750;
	
	public Q00836_RequestFromTheBlackbirdClan()
	{
		super(836);
		addStartNpc(START_NPC);
		addTalkId(START_NPC);
		addCondMinLevel(Config.PLAYER_MAXIMUM_LEVEL, getNoQuestMsg(null));
	}
}
