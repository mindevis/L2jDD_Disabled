
package events.RedLibra;

import org.l2jdd.gameserver.model.actor.Npc;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.model.quest.LongTimeEvent;

/**
 * Red Libra<br>
 * Info - http://www.lineage2.com/en/news/events/01202016-red-libra.php
 * @author Mobius
 */
public class RedLibra extends LongTimeEvent
{
	// NPCs
	private static final int RED = 34210;
	private static final int GREEN = 34211;
	private static final int BLACK = 34212;
	private static final int PINK = 34213;
	private static final int BLUE = 34214;
	
	private RedLibra()
	{
		addStartNpc(RED, GREEN, BLACK, PINK, BLUE);
		addFirstTalkId(RED, GREEN, BLACK, PINK, BLUE);
		addTalkId(RED, GREEN, BLACK, PINK, BLUE);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, PlayerInstance player)
	{
		String htmltext = null;
		switch (event)
		{
			case "34210-1.htm":
			case "34211-1.htm":
			case "34211-2.htm":
			case "34212-1.htm":
			case "34212-2.htm":
			case "34212-3.htm":
			case "34213-1.htm":
			case "34213-2.htm":
			case "34213-3.htm":
			case "34214-1.htm":
			{
				htmltext = event;
				break;
			}
		}
		return htmltext;
	}
	
	@Override
	public String onFirstTalk(Npc npc, PlayerInstance player)
	{
		return npc.getId() + "-1.htm";
	}
	
	public static void main(String[] args)
	{
		new RedLibra();
	}
}
