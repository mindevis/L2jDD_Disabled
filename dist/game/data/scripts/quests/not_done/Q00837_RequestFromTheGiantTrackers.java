
package quests.not_done;

import org.l2jdd.Config;
import org.l2jdd.gameserver.model.quest.Quest;

/**
 * @author Mobius
 */
public class Q00837_RequestFromTheGiantTrackers extends Quest
{
	private static final int START_NPC = 36751;
	
	public Q00837_RequestFromTheGiantTrackers()
	{
		super(837);
		addStartNpc(START_NPC);
		addTalkId(START_NPC);
		addCondMinLevel(Config.PLAYER_MAXIMUM_LEVEL, getNoQuestMsg(null));
	}
}
