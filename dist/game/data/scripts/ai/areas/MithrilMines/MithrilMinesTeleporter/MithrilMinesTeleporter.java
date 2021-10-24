
package ai.areas.MithrilMines.MithrilMinesTeleporter;

import org.l2jdd.gameserver.model.Location;
import org.l2jdd.gameserver.model.actor.Npc;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;

import ai.AbstractNpcAI;

/**
 * Mithril Mines teleport AI.
 * @author Charus
 */
public class MithrilMinesTeleporter extends AbstractNpcAI
{
	// NPC
	private static final int TELEPORT_CRYSTAL = 32652;
	// Location
	private static final Location[] LOCS =
	{
		new Location(171946, -173352, 3440),
		new Location(175499, -181586, -904),
		new Location(173462, -174011, 3480),
		new Location(179299, -182831, -224),
		new Location(178591, -184615, -360),
		new Location(175499, -181586, -904)
	};
	
	private MithrilMinesTeleporter()
	{
		addStartNpc(TELEPORT_CRYSTAL);
		addFirstTalkId(TELEPORT_CRYSTAL);
		addTalkId(TELEPORT_CRYSTAL);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, PlayerInstance player)
	{
		final int index = Integer.parseInt(event) - 1;
		if (LOCS.length > index)
		{
			final Location loc = LOCS[index];
			player.teleToLocation(loc, false);
		}
		return super.onAdvEvent(event, npc, player);
	}
	
	@Override
	public String onFirstTalk(Npc npc, PlayerInstance player)
	{
		if (npc.isInsideRadius2D(173147, -173762, 0, Npc.INTERACTION_DISTANCE))
		{
			return "32652-01.htm";
		}
		
		if (npc.isInsideRadius2D(181941, -174614, 0, Npc.INTERACTION_DISTANCE))
		{
			return "32652-02.htm";
		}
		
		if (npc.isInsideRadius2D(179560, -182956, 0, Npc.INTERACTION_DISTANCE))
		{
			return "32652-03.htm";
		}
		return super.onFirstTalk(npc, player);
	}
	
	public static void main(String[] args)
	{
		new MithrilMinesTeleporter();
	}
}
