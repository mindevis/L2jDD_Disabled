
package ai.areas.Parnassus.Fioren;

import org.l2jdd.gameserver.enums.Movie;
import org.l2jdd.gameserver.model.actor.Npc;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;

import ai.AbstractNpcAI;

/**
 * Fioren AI.
 * @author St3eT
 */
public class Fioren extends AbstractNpcAI
{
	// NPCs
	private static final int FIOREN = 33044;
	
	private Fioren()
	{
		addStartNpc(FIOREN);
		addTalkId(FIOREN);
		addFirstTalkId(FIOREN);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, PlayerInstance player)
	{
		if (event.equals("startMovie"))
		{
			playMovie(player, Movie.SI_BARLOG_STORY);
		}
		return super.onAdvEvent(event, npc, player);
	}
	
	public static void main(String[] args)
	{
		new Fioren();
	}
}