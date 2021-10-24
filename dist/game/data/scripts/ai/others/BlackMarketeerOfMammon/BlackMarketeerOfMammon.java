
package ai.others.BlackMarketeerOfMammon;

import java.util.StringTokenizer;

import org.l2jdd.gameserver.model.actor.Npc;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.model.itemcontainer.Inventory;
import org.l2jdd.gameserver.util.Util;

import ai.AbstractNpcAI;

/**
 * Black Marketeer of Mammon AI.
 * @author St3eT
 */
public class BlackMarketeerOfMammon extends AbstractNpcAI
{
	// NPC
	private static final int BLACK_MARKETEER = 31092;
	
	private BlackMarketeerOfMammon()
	{
		addStartNpc(BLACK_MARKETEER);
		addTalkId(BLACK_MARKETEER);
		addFirstTalkId(BLACK_MARKETEER);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, PlayerInstance player)
	{
		String htmltext = event;
		if (event.equals("31092-01.html"))
		{
			return event;
		}
		else if (event.startsWith("exchange"))
		{
			final StringTokenizer st = new StringTokenizer(event, " ");
			htmltext = st.nextToken();
			if (!st.hasMoreElements())
			{
				return "31092-02.html";
			}
			
			final String value = st.nextToken();
			if (!Util.isDigit(value))
			{
				return "31092-02.html";
			}
			
			final long count = Integer.parseInt(value);
			final long AAcount = player.getAncientAdena();
			if (count < 1)
			{
				return "31092-02.html";
			}
			
			if (count > AAcount)
			{
				return "31092-03.html";
			}
			takeItems(player, Inventory.ANCIENT_ADENA_ID, count);
			giveAdena(player, count, false);
			return "31092-04.html";
		}
		return super.onAdvEvent(htmltext, npc, player);
	}
	
	public static void main(String[] args)
	{
		new BlackMarketeerOfMammon();
	}
}
