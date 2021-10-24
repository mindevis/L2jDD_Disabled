
package ai.areas.TownOfGludio.Acateo;

import org.l2jdd.gameserver.model.actor.Npc;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;

import ai.AbstractNpcAI;

/**
 * Acateo AI.
 * @author Gladicek
 */
public class Acateo extends AbstractNpcAI
{
	// NPC
	private static final int ACATEO = 33905;
	// Item
	private static final int ACADEMY_CIRCLET = 8181;
	
	private Acateo()
	{
		addStartNpc(ACATEO);
		addFirstTalkId(ACATEO);
		addTalkId(ACATEO);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, PlayerInstance player)
	{
		if (event.equals("give_circlet"))
		{
			if (hasQuestItems(player, ACADEMY_CIRCLET))
			{
				return "33905-3.html";
			}
			giveItems(player, ACADEMY_CIRCLET, 1);
			return "33905-2.html";
		}
		return super.onAdvEvent(event, npc, player);
	}
	
	@Override
	public String onFirstTalk(Npc npc, PlayerInstance player)
	{
		return player.isAcademyMember() ? "33905-1.html" : "33905.html";
	}
	
	public static void main(String[] args)
	{
		new Acateo();
	}
}