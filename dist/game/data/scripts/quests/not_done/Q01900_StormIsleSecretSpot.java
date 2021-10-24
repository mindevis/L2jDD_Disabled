
package quests.not_done;

import org.l2jdd.Config;
import org.l2jdd.gameserver.model.quest.Quest;

/**
 * @author Mobius
 */
public class Q01900_StormIsleSecretSpot extends Quest
{
	private static final int START_NPC = 34528;
	
	public Q01900_StormIsleSecretSpot()
	{
		super(1900);
		addStartNpc(START_NPC);
		addTalkId(START_NPC);
		addCondMinLevel(Config.PLAYER_MAXIMUM_LEVEL, getNoQuestMsg(null));
	}
}
