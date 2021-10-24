
package quests.not_done;

import org.l2jdd.Config;
import org.l2jdd.gameserver.model.quest.Quest;

/**
 * @author Mobius
 */
public class Q01901_StormIsleFurtiveDeal extends Quest
{
	private static final int START_NPC = 34528;
	
	public Q01901_StormIsleFurtiveDeal()
	{
		super(1901);
		addStartNpc(START_NPC);
		addTalkId(START_NPC);
		addCondMinLevel(Config.PLAYER_MAXIMUM_LEVEL, getNoQuestMsg(null));
	}
}
