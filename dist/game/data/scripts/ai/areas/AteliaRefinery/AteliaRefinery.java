
package ai.areas.AteliaRefinery;

import org.l2jdd.gameserver.model.actor.Npc;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;

import ai.AbstractNpcAI;

/**
 * @author NviX
 */
public class AteliaRefinery extends AbstractNpcAI
{
	// NPC
	private static final int ATELIA_REFINERY_TELEPORT_DEVICE = 34441;
	// Special Mobs
	private static final int HARKE = 24161;
	private static final int ERGALION = 24162;
	private static final int SPIRA = 24163;
	// Mobs
	private static final int[] MOBS =
	{
		24144, // Death Rogue
		24145, // Death Shooter
		24146, // Death Warrior
		24147, // Death Sorcerer
		24148, // Death Pondus
		24149, // Devil Nightmare
		24150, // Devil Warrior
		24151, // Devil Guardian
		24152, // Devil Sinist
		24153, // Devil Varos
		24154, // Demonic Wizard
		24155, // Demonic Warrior
		24156, // Demonic Archer
		24157, // Demonic Keras
		24158, // Demonic Weiss
		24159, // Atelia Yuyurina
		24160 // Atelia Popobena
	};
	
	private AteliaRefinery()
	{
		addTalkId(ATELIA_REFINERY_TELEPORT_DEVICE);
		addFirstTalkId(ATELIA_REFINERY_TELEPORT_DEVICE);
		addKillId(MOBS);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, PlayerInstance player)
	{
		String htmltext = null;
		switch (event)
		{
			case "first_area":
			{
				player.teleToLocation(-59493, 52620, -8610);
				break;
			}
			case "second_area":
			{
				player.teleToLocation(-56096, 49688, -8729);
				break;
			}
			case "third_area":
			{
				player.teleToLocation(-56160, 45406, -8847);
				break;
			}
			case "fourth_area":
			{
				player.teleToLocation(-56140, 41067, -8965);
				break;
			}
			case "fifth_area":
			{
				player.teleToLocation(-251728, 178576, -8928);
				break;
			}
		}
		return htmltext;
	}
	
	@Override
	public String onKill(Npc npc, PlayerInstance killer, boolean isSummon)
	{
		int chance = 1;
		if (getRandom(10000) < chance)
		{
			addSpawn(HARKE, npc, false, 300000);
		}
		else if (getRandom(10000) < chance)
		{
			addSpawn(ERGALION, npc, false, 300000);
		}
		else if (getRandom(100000) < chance)
		{
			addSpawn(SPIRA, npc, false, 300000);
		}
		return super.onKill(npc, killer, isSummon);
	}
	
	public static void main(String[] args)
	{
		new AteliaRefinery();
	}
}
