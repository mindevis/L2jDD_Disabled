
package ai.areas.WallOfArgos.ElmoredenServantsGhost;

import org.l2jdd.gameserver.model.Location;
import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.actor.Npc;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;

import ai.AbstractNpcAI;

/**
 * Elmoreden Servant's Ghost AI.
 * @author St3eT
 */
public class ElmoredenServantsGhost extends AbstractNpcAI
{
	// NPC
	private static final int GHOST = 31920; // Elmoreden Servant's Ghost
	// Items
	private static final int USED_GRAVE_PASS = 7261;
	private static final int ANTIQUE_BROOCH = 7262;
	
	private ElmoredenServantsGhost()
	{
		addStartNpc(GHOST);
		addTalkId(GHOST);
		addFirstTalkId(GHOST);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, PlayerInstance player)
	{
		String htmltext = null;
		if (event.equals("teleport1") || event.equals("teleport2"))
		{
			if (!hasAtLeastOneQuestItem(player, USED_GRAVE_PASS, ANTIQUE_BROOCH))
			{
				htmltext = "31920-no.html";
			}
			else
			{
				takeItems(player, USED_GRAVE_PASS, 1);
				
				final Location loc;
				final StatSet npcParameters = npc.getParameters();
				if (event.equals("teleport1"))
				{
					loc = new Location(npcParameters.getInt("TelPos_X1", 0), npcParameters.getInt("TelPos_Y1", 0), npcParameters.getInt("TelPos_Z1", 0));
				}
				else
				{
					loc = new Location(npcParameters.getInt("TelPos_X2", 0), npcParameters.getInt("TelPos_Y2", 0), npcParameters.getInt("TelPos_Z2", 0));
				}
				
				player.teleToLocation(loc);
			}
		}
		return htmltext;
	}
	
	public static void main(String[] args)
	{
		new ElmoredenServantsGhost();
	}
}
