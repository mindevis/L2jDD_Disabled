
package ai.areas.KeucereusAllianceBase.Lekon;

import org.l2jdd.gameserver.instancemanager.AirShipManager;
import org.l2jdd.gameserver.model.actor.Npc;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.model.clan.Clan;
import org.l2jdd.gameserver.network.SystemMessageId;

import ai.AbstractNpcAI;

/**
 * Lekon AI.
 * @author St3eT
 */
public class Lekon extends AbstractNpcAI
{
	// NPCs
	private static final int LEKON = 32557;
	// Items
	private static final int LICENCE = 13559; // Airship Summon License
	private static final int STONE = 13277; // Energy Star Stone
	// Misc
	private static final int MIN_CLAN_LV = 5;
	private static final int STONE_COUNT = 10;
	
	public Lekon()
	{
		addFirstTalkId(LEKON);
		addTalkId(LEKON);
		addStartNpc(LEKON);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, PlayerInstance player)
	{
		String htmltext = null;
		switch (event)
		{
			case "32557-01.html":
			{
				htmltext = event;
				break;
			}
			case "licence":
			{
				final Clan clan = player.getClan();
				if ((clan == null) || !player.isClanLeader() || (clan.getLevel() < MIN_CLAN_LV))
				{
					htmltext = "32557-02.html";
				}
				else if (hasAtLeastOneQuestItem(player, LICENCE))
				{
					htmltext = "32557-04.html";
				}
				else if (AirShipManager.getInstance().hasAirShipLicense(clan.getId()))
				{
					player.sendPacket(SystemMessageId.THE_AIRSHIP_SUMMON_LICENSE_HAS_ALREADY_BEEN_ACQUIRED);
				}
				else if (getQuestItemsCount(player, STONE) >= STONE_COUNT)
				{
					takeItems(player, STONE, STONE_COUNT);
					giveItems(player, LICENCE, 1);
				}
				else
				{
					htmltext = "32557-03.html";
				}
				break;
			}
		}
		return htmltext;
	}
	
	public static void main(String[] args)
	{
		new Lekon();
	}
}
