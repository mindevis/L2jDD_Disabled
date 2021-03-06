
package ai.areas.KeucereusAllianceBase;

import java.util.Calendar;
import java.util.GregorianCalendar;

import org.l2jdd.commons.util.Chronos;
import org.l2jdd.gameserver.data.SpawnTable;
import org.l2jdd.gameserver.enums.ChatType;
import org.l2jdd.gameserver.enums.Movie;
import org.l2jdd.gameserver.model.World;
import org.l2jdd.gameserver.model.actor.Npc;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.network.NpcStringId;

import ai.AbstractNpcAI;

/**
 * Lindvior Scene AI.
 * @author nonom
 */
public class LindviorScene extends AbstractNpcAI
{
	private static final int LINDVIOR_CAMERA = 18669;
	private static final int TOMARIS = 32552;
	private static final int ARTIUS = 32559;
	
	private static final int RESET_HOUR = 18;
	private static final int RESET_MIN = 58;
	private static final int RESET_DAY_1 = Calendar.TUESDAY;
	private static final int RESET_DAY_2 = Calendar.WEDNESDAY;
	private static final int RESET_DAY_3 = Calendar.THURSDAY;
	private static final int RESET_DAY_4 = Calendar.FRIDAY;
	
	private static boolean ALT_MODE = false;
	private static int ALT_MODE_MIN = 120; // schedule delay in minutes if ALT_MODE enabled
	
	public LindviorScene()
	{
		scheduleNextLindviorVisit();
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, PlayerInstance player)
	{
		switch (event)
		{
			case "tomaris_shout1":
			{
				npc.broadcastSay(ChatType.NPC_SHOUT, NpcStringId.HUH_THE_SKY_LOOKS_FUNNY_WHAT_S_THAT);
				break;
			}
			case "artius_shout":
			{
				npc.broadcastSay(ChatType.NPC_SHOUT, NpcStringId.A_POWERFUL_SUBORDINATE_IS_BEING_HELD_BY_THE_BARRIER_ORB_THIS_REACTION_MEANS);
				break;
			}
			case "tomaris_shout2":
			{
				npc.broadcastSay(ChatType.NPC_SHOUT, NpcStringId.BE_CAREFUL_SOMETHING_S_COMING);
				break;
			}
			case "lindvior_scene":
			{
				if (npc != null)
				{
					playMovie(World.getInstance().getVisibleObjectsInRange(npc, PlayerInstance.class, 4000), Movie.SC_LINDVIOR);
				}
				break;
			}
			case "start":
			{
				final Npc lindviorCamera = SpawnTable.getInstance().getAnySpawn(LINDVIOR_CAMERA).getLastSpawn();
				final Npc tomaris = SpawnTable.getInstance().getAnySpawn(TOMARIS).getLastSpawn();
				final Npc artius = SpawnTable.getInstance().getAnySpawn(ARTIUS).getLastSpawn();
				startQuestTimer("tomaris_shout1", 1000, tomaris, null);
				startQuestTimer("artius_shout", 60000, artius, null);
				startQuestTimer("tomaris_shout2", 90000, tomaris, null);
				startQuestTimer("lindvior_scene", 120000, lindviorCamera, null);
				scheduleNextLindviorVisit();
				break;
			}
		}
		return super.onAdvEvent(event, npc, player);
	}
	
	public void scheduleNextLindviorVisit()
	{
		final long delay = (ALT_MODE) ? ALT_MODE_MIN * 120000 : scheduleNextLindviorDate();
		startQuestTimer("start", delay, null, null);
	}
	
	protected long scheduleNextLindviorDate()
	{
		final GregorianCalendar date = new GregorianCalendar();
		date.set(Calendar.MINUTE, RESET_MIN);
		date.set(Calendar.HOUR_OF_DAY, RESET_HOUR);
		if (Chronos.currentTimeMillis() >= date.getTimeInMillis())
		{
			date.add(Calendar.DAY_OF_WEEK, 1);
		}
		
		final int dayOfWeek = date.get(Calendar.DAY_OF_WEEK);
		if (dayOfWeek <= RESET_DAY_1)
		{
			date.add(Calendar.DAY_OF_WEEK, RESET_DAY_1 - dayOfWeek);
		}
		else if (dayOfWeek <= RESET_DAY_2)
		{
			date.add(Calendar.DAY_OF_WEEK, RESET_DAY_2 - dayOfWeek);
		}
		else if (dayOfWeek <= RESET_DAY_3)
		{
			date.add(Calendar.DAY_OF_WEEK, RESET_DAY_3 - dayOfWeek);
		}
		else if (dayOfWeek <= RESET_DAY_4)
		{
			date.add(Calendar.DAY_OF_WEEK, RESET_DAY_4 - dayOfWeek);
		}
		else
		{
			date.add(Calendar.DAY_OF_WEEK, 1 + RESET_DAY_1);
		}
		return date.getTimeInMillis() - Chronos.currentTimeMillis();
	}
	
	public static void main(String[] args)
	{
		new LindviorScene();
	}
}
