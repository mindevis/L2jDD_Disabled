
package ai.others.FortressArcherCaptain;

import org.l2jdd.gameserver.model.actor.Npc;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;

import ai.AbstractNpcAI;

/**
 * Fortress Archer Captain AI.
 * @author St3eT
 */
public class FortressArcherCaptain extends AbstractNpcAI
{
	// NPCs
	private static final int[] ARCHER_CAPTAIN =
	{
		35661, // Shanty Fortress
		35692, // Southern Fortress
		35730, // Hive Fortress
		35761, // Valley Fortress
		35799, // Ivory Fortress
		35830, // Narsell Fortress
		35861, // Bayou Fortress
		35899, // White Sands Fortress
		35930, // Borderland Fortress
		35968, // Swamp Fortress
		36006, // Archaic Fortress
		36037, // Floran Fortress
		36075, // Cloud Mountain
		36113, // Tanor Fortress
		36144, // Dragonspine Fortress
		36175, // Antharas's Fortress
		36213, // Western Fortress
		36251, // Hunter's Fortress
		36289, // Aaru Fortress
		36320, // Demon Fortress
		36358, // Monastic Fortress
	};
	
	private FortressArcherCaptain()
	{
		addStartNpc(ARCHER_CAPTAIN);
		addFirstTalkId(ARCHER_CAPTAIN);
	}
	
	@Override
	public String onFirstTalk(Npc npc, PlayerInstance player)
	{
		final int fortOwner = npc.getFort().getOwnerClan() == null ? 0 : npc.getFort().getOwnerClan().getId();
		return ((player.getClan() != null) && (player.getClanId() == fortOwner)) ? "FortressArcherCaptain.html" : "FortressArcherCaptain-01.html";
	}
	
	public static void main(String[] args)
	{
		new FortressArcherCaptain();
	}
}