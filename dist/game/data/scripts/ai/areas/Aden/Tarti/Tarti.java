
package ai.areas.Aden.Tarti;

import org.l2jdd.gameserver.model.actor.Npc;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.network.serverpackets.PlaySound;

import ai.AbstractNpcAI;

/**
 * Tarti AI
 * @author Gigi
 * @date 2019-08-17 - [22:44:02]
 */
public class Tarti extends AbstractNpcAI
{
	// NPC
	private static final int TARTI = 34360;
	// Misc
	private static final String[] TARTI_VOICE =
	{
		"Npcdialog1.tarti_ep50_greeting_8",
		"Npcdialog1.tarti_ep50_greeting_9",
		"Npcdialog1.tarti_ep50_greeting_10"
	};
	
	private Tarti()
	{
		addStartNpc(TARTI);
		addFirstTalkId(TARTI);
	}
	
	@Override
	public String onFirstTalk(Npc npc, PlayerInstance player)
	{
		player.sendPacket(new PlaySound(3, TARTI_VOICE[getRandom(3)], 0, 0, 0, 0, 0));
		return "34360.html";
	}
	
	public static void main(String[] args)
	{
		new Tarti();
	}
}
