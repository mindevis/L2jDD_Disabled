
package ai.bosses.Freya.FreyasSteward;

import org.l2jdd.gameserver.model.Location;
import org.l2jdd.gameserver.model.actor.Npc;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;

import ai.AbstractNpcAI;

/**
 * Freya's Steward AI.
 * @author Adry_85
 */
public class FreyasSteward extends AbstractNpcAI
{
	// NPC
	private static final int FREYAS_STEWARD = 32029;
	// Location
	private static final Location TELEPORT_LOC = new Location(103045, -124361, -2768);
	// Misc
	private static final int MIN_LEVEL = 82;
	
	private FreyasSteward()
	{
		addStartNpc(FREYAS_STEWARD);
		addFirstTalkId(FREYAS_STEWARD);
		addTalkId(FREYAS_STEWARD);
	}
	
	@Override
	public String onFirstTalk(Npc npc, PlayerInstance player)
	{
		return "32029.html";
	}
	
	@Override
	public String onTalk(Npc npc, PlayerInstance player)
	{
		if (player.getLevel() >= MIN_LEVEL)
		{
			player.teleToLocation(TELEPORT_LOC);
			return null;
		}
		return "32029-1.html";
	}
	
	public static void main(String[] args)
	{
		new FreyasSteward();
	}
}