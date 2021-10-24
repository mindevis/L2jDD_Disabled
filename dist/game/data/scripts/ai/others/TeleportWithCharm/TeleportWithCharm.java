
package ai.others.TeleportWithCharm;

import org.l2jdd.gameserver.model.Location;
import org.l2jdd.gameserver.model.actor.Npc;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;

import ai.AbstractNpcAI;

/**
 * Charm teleport AI.<br>
 * @author Plim
 */
public class TeleportWithCharm extends AbstractNpcAI
{
	// NPCs
	private static final int WHIRPY = 30540;
	private static final int TAMIL = 30576;
	// Items
	private static final int ORC_GATEKEEPER_CHARM = 1658;
	private static final int DWARF_GATEKEEPER_TOKEN = 1659;
	// Locations
	private static final Location ORC_TELEPORT = new Location(-80826, 149775, -3043);
	private static final Location DWARF_TELEPORT = new Location(-80826, 149775, -3043);
	
	private TeleportWithCharm()
	{
		addStartNpc(WHIRPY, TAMIL);
		addTalkId(WHIRPY, TAMIL);
	}
	
	@Override
	public String onTalk(Npc npc, PlayerInstance player)
	{
		switch (npc.getId())
		{
			case WHIRPY:
			{
				if (hasQuestItems(player, DWARF_GATEKEEPER_TOKEN))
				{
					takeItems(player, DWARF_GATEKEEPER_TOKEN, 1);
					player.teleToLocation(DWARF_TELEPORT);
				}
				else
				{
					return "30540-01.htm";
				}
				break;
			}
			case TAMIL:
			{
				if (hasQuestItems(player, ORC_GATEKEEPER_CHARM))
				{
					takeItems(player, ORC_GATEKEEPER_CHARM, 1);
					player.teleToLocation(ORC_TELEPORT);
				}
				else
				{
					return "30576-01.htm";
				}
				break;
			}
		}
		return super.onTalk(npc, player);
	}
	
	public static void main(String[] args)
	{
		new TeleportWithCharm();
	}
}
