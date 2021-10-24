
package ai.areas.StakatoNest.StakatoNestTeleporter;

import org.l2jdd.gameserver.model.Location;
import org.l2jdd.gameserver.model.actor.Npc;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;

import ai.AbstractNpcAI;

/**
 * Stakato Nest Teleport AI.
 * @author Charus
 */
public class StakatoNestTeleporter extends AbstractNpcAI
{
	// Locations
	private static final Location[] LOCS =
	{
		new Location(80456, -52322, -5640),
		new Location(88718, -46214, -4640),
		new Location(87464, -54221, -5120),
		new Location(80848, -49426, -5128),
		new Location(87682, -43291, -4128)
	};
	// NPC
	private static final int KINTAIJIN = 32640;
	
	private StakatoNestTeleporter()
	{
		addStartNpc(KINTAIJIN);
		addTalkId(KINTAIJIN);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, PlayerInstance player)
	{
		final int index = Integer.parseInt(event) - 1;
		if (LOCS.length > index)
		{
			final Location loc = LOCS[index];
			if (player.getParty() != null)
			{
				for (PlayerInstance partyMember : player.getParty().getMembers())
				{
					if (partyMember.isInsideRadius3D(player, 1000))
					{
						partyMember.teleToLocation(loc, true);
					}
				}
			}
			player.teleToLocation(loc, false);
		}
		return super.onAdvEvent(event, npc, player);
	}
	
	@Override
	public String onTalk(Npc npc, PlayerInstance player)
	{
		// TODO: Pre-Quest removed with Etina's Fate.
		// final QuestState accessQuest = player.getQuestState(Q00240_ImTheOnlyOneYouCanTrust.class.getSimpleName());
		// return (((accessQuest != null) && accessQuest.isCompleted()) ? "32640.htm" : "32640-no.htm");
		return "32640.htm";
	}
	
	public static void main(String[] args)
	{
		new StakatoNestTeleporter();
	}
}