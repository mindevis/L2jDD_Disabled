
package ai.areas.HellboundIsland.LeonaBlackbird;

import org.l2jdd.gameserver.enums.Movie;
import org.l2jdd.gameserver.model.actor.Npc;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;

import ai.AbstractNpcAI;

/**
 * Leona Blackbird AI.
 * @author St3eT
 */
public class LeonaBlackbird extends AbstractNpcAI
{
	// NPCs
	private static final int LEONA = 31595; // Leona Blackbird
	
	public LeonaBlackbird()
	{
		addFirstTalkId(LEONA);
		addTalkId(LEONA);
		addStartNpc(LEONA);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, PlayerInstance player)
	{
		String htmltext = null;
		switch (event)
		{
			case "31595.html":
			case "31595-01.html":
			case "31595-02.html":
			case "31595-03.html":
			{
				htmltext = event;
				break;
			}
			case "playMovie":
			{
				playMovie(player, Movie.SC_HELLBOUND);
				break;
			}
		}
		return htmltext;
	}
	
	public static void main(String[] args)
	{
		new LeonaBlackbird();
	}
}
