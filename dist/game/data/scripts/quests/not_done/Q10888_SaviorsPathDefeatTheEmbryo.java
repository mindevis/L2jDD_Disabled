
package quests.not_done;

import org.l2jdd.Config;
import org.l2jdd.gameserver.model.quest.Quest;

/**
 * @author Mobius
 */
public class Q10888_SaviorsPathDefeatTheEmbryo extends Quest
{
	private static final int START_NPC = 34427;
	
	public Q10888_SaviorsPathDefeatTheEmbryo()
	{
		super(10888);
		addStartNpc(START_NPC);
		addTalkId(START_NPC);
		addCondMinLevel(Config.PLAYER_MAXIMUM_LEVEL, getNoQuestMsg(null));
	}
}
