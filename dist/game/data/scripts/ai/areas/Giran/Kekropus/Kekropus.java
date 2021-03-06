
package ai.areas.Giran.Kekropus;

import java.util.List;

import org.l2jdd.Config;
import org.l2jdd.gameserver.instancemanager.GrandBossManager;
import org.l2jdd.gameserver.instancemanager.QuestManager;
import org.l2jdd.gameserver.model.Location;
import org.l2jdd.gameserver.model.Party;
import org.l2jdd.gameserver.model.actor.Npc;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.model.quest.Quest;
import org.l2jdd.gameserver.network.serverpackets.NpcHtmlMessage;
import org.l2jdd.gameserver.network.serverpackets.PlaySound;

import ai.AbstractNpcAI;
import ai.bosses.Helios.Helios;

/**
 * Kekropus AI
 * @author Gigi
 */
public class Kekropus extends AbstractNpcAI
{
	// NPCs
	private static final int KEKROPUS = 34222;
	private static final int HELIOS = 29305;
	// Teleports
	private static final Location NORMAL_TELEPORT = new Location(79827, 152588, 2304);
	private static final Location RAID_ENTER_LOC = new Location(79313, 153617, 2307);
	// Status
	private static final int ALIVE = 0;
	private static final int WAITING = 1;
	private static final int DEAD = 3;
	
	private Kekropus()
	{
		addStartNpc(KEKROPUS);
		addTalkId(KEKROPUS);
		addFirstTalkId(KEKROPUS);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, PlayerInstance player)
	{
		final String htmltext = null;
		switch (event)
		{
			case "teleport":
			{
				player.teleToLocation(NORMAL_TELEPORT);
				break;
			}
			case "helios":
			{
				final int status = GrandBossManager.getInstance().getBossStatus(HELIOS);
				if (player.isGM())
				{
					player.teleToLocation(RAID_ENTER_LOC, true);
				}
				else
				{
					if ((status > ALIVE) && (status < DEAD))
					{
						return "34222-03.html";
					}
					if (status == DEAD)
					{
						return "34222-04.html";
					}
					if (!player.isInParty())
					{
						final NpcHtmlMessage packet = new NpcHtmlMessage(npc.getObjectId());
						packet.setHtml(getHtm(player, "34222-01.html"));
						packet.replace("%min%", Integer.toString(Config.HELIOS_MIN_PLAYER));
						packet.replace("%minLevel%", Integer.toString(Config.HELIOS_MIN_PLAYER_LEVEL));
						player.sendPacket(packet);
						return null;
					}
					final Party party = player.getParty();
					final boolean isInCC = party.isInCommandChannel();
					final List<PlayerInstance> members = (isInCC) ? party.getCommandChannel().getMembers() : party.getMembers();
					final boolean isPartyLeader = (isInCC) ? party.getCommandChannel().isLeader(player) : party.isLeader(player);
					if (!isPartyLeader)
					{
						return "34222-02.html";
					}
					if (members.size() < Config.HELIOS_MIN_PLAYER)
					{
						final NpcHtmlMessage packet = new NpcHtmlMessage(npc.getObjectId());
						packet.setHtml(getHtm(player, "34222-01.html"));
						packet.replace("%min%", Integer.toString(Config.HELIOS_MIN_PLAYER));
						packet.replace("%minLevel%", Integer.toString(Config.HELIOS_MIN_PLAYER_LEVEL));
						player.sendPacket(packet);
						return null;
					}
					for (PlayerInstance member : members)
					{
						if (member.getLevel() < Config.HELIOS_MIN_PLAYER_LEVEL)
						{
							final NpcHtmlMessage packet = new NpcHtmlMessage(npc.getObjectId());
							packet.setHtml(getHtm(player, "34222-01.html"));
							packet.replace("%min%", Integer.toString(Config.HELIOS_MIN_PLAYER));
							packet.replace("%minLevel%", Integer.toString(Config.HELIOS_MIN_PLAYER_LEVEL));
							player.sendPacket(packet);
							return null;
						}
					}
					for (PlayerInstance member : members)
					{
						if ((member.calculateDistance2D(npc) < 1000) && (npc.getId() == KEKROPUS))
						{
							member.teleToLocation(RAID_ENTER_LOC, true);
						}
					}
				}
				if (status == ALIVE)
				{
					GrandBossManager.getInstance().setBossStatus(HELIOS, WAITING);
					heliosAI().startQuestTimer("beginning", Config.HELIOS_WAIT_TIME * 60000, null, null);
				}
				break;
			}
		}
		return htmltext;
	}
	
	@Override
	public String onFirstTalk(Npc npc, PlayerInstance player)
	{
		final int i = getRandom(0, 12);
		if ((i > 0) && (i <= 3))
		{
			player.sendPacket(new PlaySound(3, "Npcdialog1.kekrops_greeting_8", 0, 0, 0, 0, 0));
		}
		else if ((i > 3) && (i <= 6))
		{
			player.sendPacket(new PlaySound(3, "Npcdialog1.kekrops_greeting_7", 0, 0, 0, 0, 0));
		}
		else if ((i > 6) && (i <= 9))
		{
			player.sendPacket(new PlaySound(3, "Npcdialog1.kekrops_greeting_6", 0, 0, 0, 0, 0));
		}
		else
		{
			player.sendPacket(new PlaySound(3, "Npcdialog1.kekrops_greeting_5", 0, 0, 0, 0, 0));
		}
		return "34222.html";
	}
	
	private Quest heliosAI()
	{
		return QuestManager.getInstance().getQuest(Helios.class.getSimpleName());
	}
	
	public static void main(String[] args)
	{
		new Kekropus();
	}
}
