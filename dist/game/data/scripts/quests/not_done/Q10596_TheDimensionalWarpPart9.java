
package quests.not_done;

import org.l2jdd.Config;
import org.l2jdd.gameserver.model.quest.Quest;

/**
 * @author Mobius
 */
public class Q10596_TheDimensionalWarpPart9 extends Quest
{
	private static final int START_NPC = 33974;
	
	public Q10596_TheDimensionalWarpPart9()
	{
		super(10596);
		addStartNpc(START_NPC);
		addTalkId(START_NPC);
		addCondMinLevel(Config.PLAYER_MAXIMUM_LEVEL, getNoQuestMsg(null));
	}
}
