
package quests.not_done;

import org.l2jdd.Config;
import org.l2jdd.gameserver.model.quest.Quest;

/**
 * @author Mobius
 */
public class Q00840_RequestFromTheKingdomsRoyalGuard extends Quest
{
	private static final int START_NPC = 36754;
	
	public Q00840_RequestFromTheKingdomsRoyalGuard()
	{
		super(840);
		addStartNpc(START_NPC);
		addTalkId(START_NPC);
		addCondMinLevel(Config.PLAYER_MAXIMUM_LEVEL, getNoQuestMsg(null));
	}
}
