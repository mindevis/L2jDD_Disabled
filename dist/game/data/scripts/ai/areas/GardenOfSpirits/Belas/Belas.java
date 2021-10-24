
package ai.areas.GardenOfSpirits.Belas;

import org.l2jdd.gameserver.model.Location;
import org.l2jdd.gameserver.model.actor.Npc;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;

import ai.AbstractNpcAI;

/**
 * Teleporter Belas AI
 * @author Gigi
 */
public class Belas extends AbstractNpcAI
{
	// NPC
	private static final int BELAS = 34056;
	// Teleports
	private static final Location EAST = new Location(-41168, 79507, -4000);
	private static final Location WEST = new Location(-59485, 79782, -4104);
	// Item
	// private static final int MARK_OF_TRUST_MID_GRADE = 45843;
	// private static final int MARK_OF_TRUST_HIGH_GRADE = 45848;
	
	private Belas()
	{
		addFirstTalkId(BELAS);
		addTalkId(BELAS);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, PlayerInstance player)
	{
		String htmltext = null;
		switch (event)
		{
			case "34056-01.html":
			{
				htmltext = event;
				break;
			}
			case "West":
			{
				// if (hasQuestItems(player, MARK_OF_TRUST_MID_GRADE) || hasQuestItems(player, MARK_OF_TRUST_HIGH_GRADE))
				// {
					player.teleToLocation(WEST);
				// 	break;
				// }
				// htmltext = "34056-02.html";
				break;
			}
			case "East":
			{
				// if (hasQuestItems(player, MARK_OF_TRUST_MID_GRADE) || hasQuestItems(player, MARK_OF_TRUST_HIGH_GRADE))
				// {
					player.teleToLocation(EAST);
				// 	break;
				// }
				// htmltext = "34056-02.html";
				break;
			}
		}
		return htmltext;
	}
	
	@Override
	public String onFirstTalk(Npc npc, PlayerInstance player)
	{
		return "34056.html";
	}
	
	public static void main(String[] args)
	{
		new Belas();
	}
}