
package events.CharacterBirthday;

import org.l2jdd.gameserver.model.actor.Npc;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.util.Util;

import ai.AbstractNpcAI;

/**
 * Character Birthday event AI.<br>
 * Updated to H5 by Nyaran.
 * @author Gnacik
 */
public class CharacterBirthday extends AbstractNpcAI
{
	private static final int ALEGRIA = 32600;
	private static int SPAWNS = 0;
	
	private static final int[] GK =
	{
		30006,
		30059,
		30080,
		30134,
		30146,
		30177,
		30233,
		30256,
		30320,
		30540,
		30576,
		30836,
		30848,
		30878,
		30899,
		31275,
		31320,
		31964,
		32163
	};
	
	private CharacterBirthday()
	{
		addStartNpc(ALEGRIA);
		addStartNpc(GK);
		addTalkId(ALEGRIA);
		addTalkId(GK);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, PlayerInstance player)
	{
		String htmltext = event;
		if (event.equalsIgnoreCase("despawn_npc"))
		{
			npc.doDie(player);
			SPAWNS--;
			
			htmltext = null;
		}
		else if (event.equalsIgnoreCase("change"))
		{
			// Change Hat
			if (hasQuestItems(player, 10250))
			{
				takeItems(player, 10250, 1); // Adventurer Hat (Event)
				giveItems(player, 21594, 1); // Birthday Hat
				htmltext = null; // FIXME: Probably has html
				// Despawn npc
				npc.doDie(player);
				SPAWNS--;
			}
			else
			{
				htmltext = "32600-nohat.htm";
			}
		}
		return htmltext;
	}
	
	@Override
	public String onTalk(Npc npc, PlayerInstance player)
	{
		if (SPAWNS >= 3)
		{
			return "busy.htm";
		}
		
		if (!Util.checkIfInRange(10, npc, player, true))
		{
			final Npc spawned = addSpawn(32600, player.getX() + 10, player.getY() + 10, player.getZ() + 10, 0, false, 0, true);
			startQuestTimer("despawn_npc", 180000, spawned, player);
			SPAWNS++;
		}
		else
		{
			return "tooclose.htm";
		}
		return null;
	}
	
	public static void main(String[] args)
	{
		new CharacterBirthday();
	}
}
