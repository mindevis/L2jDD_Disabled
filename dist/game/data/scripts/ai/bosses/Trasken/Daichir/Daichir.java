
package ai.bosses.Trasken.Daichir;

import java.util.List;

import org.l2jdd.Config;
import org.l2jdd.gameserver.instancemanager.GrandBossManager;
import org.l2jdd.gameserver.model.Location;
import org.l2jdd.gameserver.model.Party;
import org.l2jdd.gameserver.model.actor.Npc;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.network.serverpackets.NpcHtmlMessage;

import ai.AbstractNpcAI;

/**
 * Daichir Teleporter AI
 * @author Mobius
 */
public class Daichir extends AbstractNpcAI
{
	// NPCs
	private static final int DAICHIR = 30537;
	private static final int TRASKEN = 29197;
	// Locations
	private static final Location ENTER_LOCATION = new Location(75445, -182112, -9880);
	// Status
	private static final int FIGHTING = 1;
	private static final int DEAD = 3;
	
	public Daichir()
	{
		addFirstTalkId(DAICHIR);
		addTalkId(DAICHIR);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, PlayerInstance player)
	{
		if (event.equals("enterEarthWyrnCave"))
		{
			final int status = GrandBossManager.getInstance().getBossStatus(TRASKEN);
			if (player.isGM())
			{
				player.teleToLocation(ENTER_LOCATION, true);
				GrandBossManager.getInstance().setBossStatus(TRASKEN, FIGHTING);
			}
			else
			{
				if (status == FIGHTING)
				{
					return "30537-1.html";
				}
				if (status == DEAD)
				{
					return "30537-2.html";
				}
				if (!player.isInParty())
				{
					return "30537-3.html";
				}
				final Party party = player.getParty();
				final boolean isInCC = party.isInCommandChannel();
				final List<PlayerInstance> members = (isInCC) ? party.getCommandChannel().getMembers() : party.getMembers();
				final boolean isPartyLeader = (isInCC) ? party.getCommandChannel().isLeader(player) : party.isLeader(player);
				if (!isPartyLeader)
				{
					return "30537-3.html";
				}
				if ((members.size() < Config.TRASKEN_MIN_PLAYERS) || (members.size() > Config.TRASKEN_MAX_PLAYERS))
				{
					final NpcHtmlMessage packet = new NpcHtmlMessage(npc.getObjectId());
					packet.setHtml(getHtm(player, "30537-4.html"));
					packet.replace("%min%", Integer.toString(Config.TRASKEN_MIN_PLAYERS));
					packet.replace("%max%", Integer.toString(Config.TRASKEN_MAX_PLAYERS));
					player.sendPacket(packet);
					return null;
				}
				for (PlayerInstance member : members)
				{
					if (member.getLevel() < Config.TRASKEN_MIN_PLAYER_LEVEL)
					{
						final NpcHtmlMessage packet = new NpcHtmlMessage(npc.getObjectId());
						packet.setHtml(getHtm(player, "30537-5.html"));
						packet.replace("%minLevel%", Integer.toString(Config.TRASKEN_MIN_PLAYER_LEVEL));
						player.sendPacket(packet);
						return null;
					}
				}
				for (PlayerInstance member : members)
				{
					if (member.isInsideRadius3D(npc, Config.ALT_PARTY_RANGE))
					{
						member.teleToLocation(ENTER_LOCATION, true);
						GrandBossManager.getInstance().setBossStatus(TRASKEN, FIGHTING);
					}
				}
			}
		}
		return null;
	}
	
	@Override
	public String onFirstTalk(Npc npc, PlayerInstance player)
	{
		return npc.getId() + ".html";
	}
	
	public static void main(String[] args)
	{
		new Daichir();
	}
}
