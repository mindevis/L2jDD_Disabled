
package events.Halloween;

import org.l2jdd.gameserver.model.actor.Npc;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.model.quest.LongTimeEvent;

/**
 * @author Mobius
 */
public class Halloween extends LongTimeEvent
{
	// NPC
	private static final int PUMPKIN_GHOST = 13135;
	
	private Halloween()
	{
		addStartNpc(PUMPKIN_GHOST);
		addFirstTalkId(PUMPKIN_GHOST);
		addTalkId(PUMPKIN_GHOST);
	}
	
	@Override
	public String onFirstTalk(Npc npc, PlayerInstance player)
	{
		return "13135-01.htm";
	}
	
	public static void main(String[] args)
	{
		new Halloween();
	}
}
