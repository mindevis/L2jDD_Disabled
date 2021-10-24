
package quests.not_done;

import org.l2jdd.Config;
import org.l2jdd.gameserver.model.quest.Quest;

/**
 * @author Mobius
 */
public class Q00838_RequestFromTheMotherTreeGuardians extends Quest
{
	private static final int START_NPC = 36752;
	
	public Q00838_RequestFromTheMotherTreeGuardians()
	{
		super(838);
		addStartNpc(START_NPC);
		addTalkId(START_NPC);
		addCondMinLevel(Config.PLAYER_MAXIMUM_LEVEL, getNoQuestMsg(null));
	}
}
