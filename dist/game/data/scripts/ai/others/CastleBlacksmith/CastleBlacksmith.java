
package ai.others.CastleBlacksmith;

import org.l2jdd.gameserver.model.PlayerCondOverride;
import org.l2jdd.gameserver.model.actor.Npc;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.model.clan.ClanPrivilege;

import ai.AbstractNpcAI;

/**
 * Castle Blacksmith AI.
 * @author malyelfik
 */
public class CastleBlacksmith extends AbstractNpcAI
{
	// Blacksmith IDs
	private static final int[] NPCS =
	{
		35098, // Blacksmith (Gludio)
		35140, // Blacksmith (Dion)
		35182, // Blacksmith (Giran)
		35224, // Blacksmith (Oren)
		35272, // Blacksmith (Aden)
		35314, // Blacksmith (Innadril)
		35361, // Blacksmith (Goddard)
		35507, // Blacksmith (Rune)
		35553, // Blacksmith (Schuttgart)
	};
	
	private CastleBlacksmith()
	{
		addStartNpc(NPCS);
		addTalkId(NPCS);
		addFirstTalkId(NPCS);
	}
	
	private boolean hasRights(PlayerInstance player, Npc npc)
	{
		final boolean isMyLord = player.isClanLeader() && (player.getClan().getCastleId() == (npc.getCastle() != null ? npc.getCastle().getResidenceId() : -1));
		return player.canOverrideCond(PlayerCondOverride.CASTLE_CONDITIONS) || isMyLord || ((player.getClanId() == npc.getCastle().getOwnerId()) && player.hasClanPrivilege(ClanPrivilege.CS_MANOR_ADMIN));
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, PlayerInstance player)
	{
		return (event.equalsIgnoreCase(npc.getId() + "-02.html") && hasRights(player, npc)) ? event : null;
	}
	
	@Override
	public String onFirstTalk(Npc npc, PlayerInstance player)
	{
		return (hasRights(player, npc)) ? npc.getId() + "-01.html" : "no.html";
	}
	
	public static void main(String[] args)
	{
		new CastleBlacksmith();
	}
}