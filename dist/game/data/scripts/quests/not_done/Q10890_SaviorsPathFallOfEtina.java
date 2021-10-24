
package quests.not_done;

import org.l2jdd.Config;
import org.l2jdd.gameserver.model.quest.Quest;

/**
 * @author Mobius
 */
public class Q10890_SaviorsPathFallOfEtina extends Quest
{
	private static final int START_NPC = 34425;
	
	public Q10890_SaviorsPathFallOfEtina()
	{
		super(10890);
		addStartNpc(START_NPC);
		addTalkId(START_NPC);
		addCondMinLevel(Config.PLAYER_MAXIMUM_LEVEL, getNoQuestMsg(null));
	}
}
