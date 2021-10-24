
package ai.bosses.Lindvior.KatoSicanus;

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
 * Kato Sicanus Teleporter AI
 * @author Gigi
 * @date 2017-07-13 - [22:17:16]
 */
public class KatoSicanus extends AbstractNpcAI
{
	// NPCs
	private static final int KATO_SICANUS = 33881;
	private static final int LINDVIOR_RAID = 29240;
	private static final int INVISIBLE = 8572;
	// Location
	private static final Location LINDVIOR_LOCATION = new Location(46929, -28807, -1400);
	
	public KatoSicanus()
	{
		addFirstTalkId(KATO_SICANUS);
		addTalkId(KATO_SICANUS);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, PlayerInstance player)
	{
		if (event.equals("teleport"))
		{
			final int status = GrandBossManager.getInstance().getBossStatus(LINDVIOR_RAID);
			if (player.isGM())
			{
				player.teleToLocation(LINDVIOR_LOCATION, true);
				addSpawn(INVISIBLE, 46707, -28586, -1400, 0, false, 60000, false);
				GrandBossManager.getInstance().setBossStatus(LINDVIOR_RAID, 1);
			}
			else
			{
				if (status == 2)
				{
					return "33881-1.html";
				}
				if (status == 3)
				{
					return "33881-2.html";
				}
				if (!player.isInParty())
				{
					return "33881-3.html";
				}
				final Party party = player.getParty();
				final boolean isInCC = party.isInCommandChannel();
				final List<PlayerInstance> members = (isInCC) ? party.getCommandChannel().getMembers() : party.getMembers();
				final boolean isPartyLeader = (isInCC) ? party.getCommandChannel().isLeader(player) : party.isLeader(player);
				for (PlayerInstance member : members)
				{
					if (!member.isInsideRadius3D(npc, Config.ALT_PARTY_RANGE))
					{
						return "33881-4.html";
					}
				}
				if (!isPartyLeader)
				{
					return "33881-3.html";
				}
				if ((members.size() < Config.LINDVIOR_MIN_PLAYERS) || (members.size() > Config.LINDVIOR_MAX_PLAYERS))
				{
					final NpcHtmlMessage packet = new NpcHtmlMessage(npc.getObjectId());
					packet.setHtml(getHtm(player, "33881-4.html"));
					packet.replace("%min%", Integer.toString(Config.LINDVIOR_MIN_PLAYERS));
					packet.replace("%max%", Integer.toString(Config.LINDVIOR_MAX_PLAYERS));
					player.sendPacket(packet);
					return null;
				}
				for (PlayerInstance member : members)
				{
					if (member.getLevel() < Config.LINDVIOR_MIN_PLAYER_LEVEL)
					{
						final NpcHtmlMessage packet = new NpcHtmlMessage(npc.getObjectId());
						packet.setHtml(getHtm(player, "33881-5.html"));
						packet.replace("%minLevel%", Integer.toString(Config.LINDVIOR_MIN_PLAYER_LEVEL));
						player.sendPacket(packet);
						return null;
					}
				}
				for (PlayerInstance member : members)
				{
					if (member.isInsideRadius3D(npc, Config.ALT_PARTY_RANGE))
					{
						member.teleToLocation(LINDVIOR_LOCATION, true);
						addSpawn(INVISIBLE, 46707, -28586, -1400, 0, false, 0, false);
						GrandBossManager.getInstance().setBossStatus(LINDVIOR_RAID, 1);
					}
				}
			}
		}
		return null;
	}
	
	@Override
	public String onFirstTalk(Npc npc, PlayerInstance player)
	{
		return "33881.html";
	}
	
	public static void main(String[] args)
	{
		new KatoSicanus();
	}
}
