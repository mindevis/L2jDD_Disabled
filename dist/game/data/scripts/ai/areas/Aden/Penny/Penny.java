
package ai.areas.Aden.Penny;

import org.l2jdd.gameserver.model.actor.Npc;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.network.serverpackets.PlaySound;

import ai.AbstractNpcAI;

/**
 * Aden Faction Npc AI
 * @author NightBR
 * @date 2019-03-27
 */
public class Penny extends AbstractNpcAI
{
	// NPC
	private static final int PENNY = 34413;
	// Misc
	private static final String[] RANDOM_VOICE =
	{
		"Npcdialog1.peny_ep50_greeting_7",
		"Npcdialog1.peny_ep50_greeting_8",
		"Npcdialog1.peny_ep50_greeting_9"
	};
	
	private Penny()
	{
		addStartNpc(PENNY);
		addTalkId(PENNY);
		addFirstTalkId(PENNY);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, PlayerInstance player)
	{
		switch (event)
		{
			case "medal":
			{
				// Take medal / Give rep?
				return null;
			}
			case "grand_medal":
			{
				// Take medal / Give rep?
				return null;
			}
		}
		return null;
	}
	
	@Override
	public String onFirstTalk(Npc npc, PlayerInstance player)
	{
		player.sendPacket(new PlaySound(3, RANDOM_VOICE[getRandom(3)], 0, 0, 0, 0, 0));
		return "34413.html";
	}
	
	public static void main(String[] args)
	{
		new Penny();
	}
}
