
package instances.FortressOfTheDead;

import org.l2jdd.gameserver.enums.Movie;
import org.l2jdd.gameserver.model.Location;
import org.l2jdd.gameserver.model.actor.Npc;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.model.instancezone.Instance;
import org.l2jdd.gameserver.model.quest.QuestState;
import org.l2jdd.gameserver.network.NpcStringId;
import org.l2jdd.gameserver.network.serverpackets.ExShowScreenMessage;

import instances.AbstractInstance;
import quests.Q11026_PathOfDestinyConviction.Q11026_PathOfDestinyConviction;

/**
 * Fortress of the Dead instance zone.
 * @author Gladicek
 */
public class FortressOfTheDead extends AbstractInstance
{
	// NPCs
	private static final int VAMPIRIC_SOLDIER = 19567;
	private static final int VON_HELLMAN = 19566;
	private static final int MYSTERIOUS_WIZARD = 33980;
	private static final int KAIN_VAN_HALTER = 33979;
	// Items
	private static final int KAIN_PROPHECY_MACHINE_FRAGMENT = 39538;
	// Location
	private static final Location VON_HELLMAN_LOC = new Location(57963, -28676, 568, 49980);
	private static final Location MYSTERIOUS_WIZARD_LOC = new Location(57982, -28645, 568);
	private static final Location KAIN_VAN_HALTER_LOC = new Location(57963, -28676, 568, 49980);
	// Misc
	private static final int TEMPLATE_ID = 254;
	
	public FortressOfTheDead()
	{
		super(TEMPLATE_ID);
		addStartNpc(KAIN_VAN_HALTER);
		addFirstTalkId(KAIN_VAN_HALTER, MYSTERIOUS_WIZARD);
		addTalkId(KAIN_VAN_HALTER, MYSTERIOUS_WIZARD);
		addKillId(VAMPIRIC_SOLDIER, VON_HELLMAN);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, PlayerInstance player)
	{
		String htmltext = null;
		if (event.equals("enterInstance"))
		{
			enterInstance(player, npc, TEMPLATE_ID);
		}
		else
		{
			final Instance world = npc.getInstanceWorld();
			if (isInInstance(world))
			{
				switch (event)
				{
					case "33979-01.html":
					case "33979-02.html":
					case "33979-03.html":
					case "33979-04.html":
					case "33979-05.html":
					case "33979-06.html":
					case "33979-07.html":
					case "33979-08.html":
					case "33979-09.html":
					case "33979-10.html":
					{
						htmltext = event;
						break;
					}
					case "exitInstance":
					{
						world.finishInstance(0);
						break;
					}
					case "vampire_dead":
					{
						addSpawn(VON_HELLMAN, VON_HELLMAN_LOC, false, 0, false, world.getId());
						break;
					}
					case "hellman_dead":
					{
						addSpawn(KAIN_VAN_HALTER, KAIN_VAN_HALTER_LOC, false, 0, false, world.getId());
						break;
					}
					case "spawnWizard":
					{
						showOnScreenMsg(player, NpcStringId.TALK_TO_THE_MYSTERIOUS_WIZARD, ExShowScreenMessage.TOP_CENTER, 5000);
						final Npc wizzard = addSpawn(MYSTERIOUS_WIZARD, MYSTERIOUS_WIZARD_LOC, true, 0, false, world.getId());
						wizzard.setSummoner(player);
						wizzard.setTitle(player.getAppearance().getVisibleName());
						wizzard.broadcastInfo();
						htmltext = "33979-11.html";
						break;
					}
					case "endCinematic":
					{
						final QuestState qs = player.getQuestState(Q11026_PathOfDestinyConviction.class.getSimpleName());
						if ((qs != null) && qs.isCond(18))
						{
							qs.setCond(19, true);
							giveItems(player, KAIN_PROPHECY_MACHINE_FRAGMENT, 1);
						}
						world.getNpc(KAIN_VAN_HALTER).deleteMe();
						world.getNpc(MYSTERIOUS_WIZARD).deleteMe();
						playMovie(player, Movie.ERT_QUEST_B);
						startQuestTimer("exitInstance", 25000, npc, player);
						break;
					}
				}
			}
		}
		return htmltext;
	}
	
	@Override
	public String onKill(Npc npc, PlayerInstance player, boolean isSummon)
	{
		final Instance world = npc.getInstanceWorld();
		if (isInInstance(world))
		{
			if (npc.getId() == VAMPIRIC_SOLDIER)
			{
				if (world.getAliveNpcs(VAMPIRIC_SOLDIER).isEmpty())
				{
					startQuestTimer("vampire_dead", 180, npc, player);
				}
			}
			else if (npc.getId() == VON_HELLMAN)
			{
				npc.deleteMe();
				playMovie(player, Movie.ERT_QUEST_A);
				startQuestTimer("hellman_dead", 180, npc, player);
			}
		}
		return super.onKill(npc, player, isSummon);
	}
	
	@Override
	public String onFirstTalk(Npc npc, PlayerInstance player)
	{
		final QuestState qs = player.getQuestState(Q11026_PathOfDestinyConviction.class.getSimpleName());
		String htmltext = null;
		switch (npc.getId())
		{
			case KAIN_VAN_HALTER:
			{
				if ((qs != null) && qs.isCond(18))
				{
					htmltext = "33979.html";
				}
				break;
			}
			case MYSTERIOUS_WIZARD:
			{
				if ((qs != null) && qs.isCond(18))
				{
					htmltext = "33980.html";
				}
				break;
			}
		}
		return htmltext;
	}
	
	public static void main(String[] args)
	{
		new FortressOfTheDead();
	}
}
