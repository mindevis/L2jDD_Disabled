
package ai.areas.Heine;

import org.l2jdd.gameserver.model.Location;
import org.l2jdd.gameserver.model.actor.Npc;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;

import ai.AbstractNpcAI;

/**
 * Warp Gate AI.
 * @author Gigi
 */
public class WrapGate extends AbstractNpcAI
{
	// NPC
	private static final int WRAP_GATE = 33900;
	// Location
	private static final Location TELEPORT_LOC = new Location(-28575, 255984, -2195);
	
	private WrapGate()
	{
		addStartNpc(WRAP_GATE);
		addFirstTalkId(WRAP_GATE);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, PlayerInstance player)
	{
		if ("enter_hellbound".equals(event))
		{
			// final QuestState qs = player.getQuestState(Q10455_ElikiasLetter.class.getSimpleName());
			// if ((qs != null) && qs.isCond(1))
			// {
			// playMovie(player, Movie.SC_HELLBOUND);
			// }
			player.teleToLocation(TELEPORT_LOC);
		}
		return null;
	}
	
	@Override
	public String onFirstTalk(Npc npc, PlayerInstance player)
	{
		return "33900.html";
	}
	
	public static void main(String[] args)
	{
		new WrapGate();
	}
}