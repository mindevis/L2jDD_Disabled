
package ai.areas.AncientCityArcan.Mumu;

import org.l2jdd.gameserver.enums.Movie;
import org.l2jdd.gameserver.model.actor.Npc;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;

import ai.AbstractNpcAI;

/**
 * Mumu AI.
 * @author St3eT
 */
public class Mumu extends AbstractNpcAI
{
	// NPC
	private static final int MUMU = 32900; // Mumu
	
	public Mumu()
	{
		addStartNpc(MUMU);
		addFirstTalkId(MUMU);
		addTalkId(MUMU);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, PlayerInstance player)
	{
		String htmltext = null;
		switch (event)
		{
			case "32900-1.html":
			{
				htmltext = event;
				break;
			}
			case "playMovie":
			{
				playMovie(player, Movie.SI_ARKAN_ENTER);
				break;
			}
		}
		return htmltext;
	}
	
	public static void main(String[] args)
	{
		new Mumu();
	}
}