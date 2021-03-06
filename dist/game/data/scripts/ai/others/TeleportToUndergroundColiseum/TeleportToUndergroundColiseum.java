
package ai.others.TeleportToUndergroundColiseum;

import org.l2jdd.commons.util.CommonUtil;
import org.l2jdd.gameserver.model.Location;
import org.l2jdd.gameserver.model.actor.Npc;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.util.Util;

import ai.AbstractNpcAI;

/**
 * Underground Coliseum teleport AI
 * @author malyelfik
 */
public class TeleportToUndergroundColiseum extends AbstractNpcAI
{
	// NPCs
	private static final int COLISEUM_HELPER = 32491;
	private static final int PADDIES = 32378;
	private static final int[] MANAGERS =
	{
		32377,
		32513,
		32514,
		32515,
		32516
	};
	
	// Locations
	private static final Location[] COLISEUM_LOCS =
	{
		new Location(-81896, -49589, -10352),
		new Location(-82271, -49196, -10352),
		new Location(-81886, -48784, -10352),
		new Location(-81490, -49167, -10352)
	};
	
	private static final Location[] RETURN_LOCS =
	{
		new Location(-59161, -56954, -2036),
		new Location(-59155, -56831, -2036),
		new Location(-59299, -56955, -2036),
		new Location(-59224, -56837, -2036),
		new Location(-59134, -56899, -2036)
	};
	
	private static final Location[][] MANAGERS_LOCS =
	{
		{
			new Location(-84451, -45452, -10728),
			new Location(-84580, -45587, -10728)
		},
		{
			new Location(-86154, -50429, -10728),
			new Location(-86118, -50624, -10728)
		},
		{
			new Location(-82009, -53652, -10728),
			new Location(-81802, -53665, -10728)
		},
		{
			new Location(-77603, -50673, -10728),
			new Location(-77586, -50503, -10728)
		},
		{
			new Location(-79186, -45644, -10728),
			new Location(-79309, -45561, -10728)
		}
	};
	
	private TeleportToUndergroundColiseum()
	{
		addStartNpc(MANAGERS);
		addStartNpc(COLISEUM_HELPER, PADDIES);
		addFirstTalkId(COLISEUM_HELPER);
		addTalkId(MANAGERS);
		addTalkId(COLISEUM_HELPER, PADDIES);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, PlayerInstance player)
	{
		if (event.endsWith(".htm"))
		{
			return event;
		}
		else if (event.equals("return"))
		{
			player.teleToLocation(getRandomEntry(RETURN_LOCS), false);
		}
		else if (Util.isDigit(event))
		{
			final int val = Integer.parseInt(event) - 1;
			player.teleToLocation(getRandomEntry(MANAGERS_LOCS[val]), false);
		}
		return null;
	}
	
	@Override
	public String onTalk(Npc npc, PlayerInstance player)
	{
		if (CommonUtil.contains(MANAGERS, npc.getId()))
		{
			player.teleToLocation(getRandomEntry(RETURN_LOCS), false);
		}
		else
		{
			player.teleToLocation(getRandomEntry(COLISEUM_LOCS), false);
		}
		return null;
	}
	
	@Override
	public String onFirstTalk(Npc npc, PlayerInstance player)
	{
		return "32491.htm";
	}
	
	public static void main(String[] args)
	{
		new TeleportToUndergroundColiseum();
	}
}