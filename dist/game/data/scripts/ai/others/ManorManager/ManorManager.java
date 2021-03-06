
package ai.others.ManorManager;

import org.l2jdd.Config;
import org.l2jdd.gameserver.instancemanager.CastleManorManager;
import org.l2jdd.gameserver.model.PlayerCondOverride;
import org.l2jdd.gameserver.model.actor.Npc;
import org.l2jdd.gameserver.model.actor.instance.MerchantInstance;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.model.events.EventType;
import org.l2jdd.gameserver.model.events.ListenerRegisterType;
import org.l2jdd.gameserver.model.events.annotations.Id;
import org.l2jdd.gameserver.model.events.annotations.RegisterEvent;
import org.l2jdd.gameserver.model.events.annotations.RegisterType;
import org.l2jdd.gameserver.model.events.impl.creature.npc.OnNpcManorBypass;
import org.l2jdd.gameserver.network.SystemMessageId;
import org.l2jdd.gameserver.network.serverpackets.BuyListSeed;
import org.l2jdd.gameserver.network.serverpackets.ExShowCropInfo;
import org.l2jdd.gameserver.network.serverpackets.ExShowManorDefaultInfo;
import org.l2jdd.gameserver.network.serverpackets.ExShowProcureCropDetail;
import org.l2jdd.gameserver.network.serverpackets.ExShowSeedInfo;
import org.l2jdd.gameserver.network.serverpackets.ExShowSellCropList;
import org.l2jdd.gameserver.network.serverpackets.SystemMessage;

import ai.AbstractNpcAI;

/**
 * Manor manager AI.
 * @author malyelfik
 */
public class ManorManager extends AbstractNpcAI
{
	private static final int[] NPC =
	{
		35644,
		35645,
		35319,
		35366,
		36456,
		35512,
		35558,
		35229,
		35230,
		35231,
		35277,
		35103,
		35145,
		35187
	};
	
	public ManorManager()
	{
		addStartNpc(NPC);
		addFirstTalkId(NPC);
		addTalkId(NPC);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, PlayerInstance player)
	{
		String htmltext = null;
		switch (event)
		{
			case "manager-help-01.htm":
			case "manager-help-02.htm":
			case "manager-help-03.htm":
			{
				htmltext = event;
				break;
			}
		}
		return htmltext;
	}
	
	@Override
	public String onFirstTalk(Npc npc, PlayerInstance player)
	{
		if (Config.ALLOW_MANOR)
		{
			final int castleId = npc.getParameters().getInt("manor_id", -1);
			if (!player.canOverrideCond(PlayerCondOverride.CASTLE_CONDITIONS) && player.isClanLeader() && (castleId == player.getClan().getCastleId()))
			{
				return "manager-lord.htm";
			}
			return "manager.htm";
		}
		return getHtm(player, "data/html/npcdefault.htm");
	}
	
	// @formatter:off
	@RegisterEvent(EventType.ON_NPC_MANOR_BYPASS)
	@RegisterType(ListenerRegisterType.NPC)
	@Id({35644, 35645, 35319, 35366, 36456, 35512, 35558, 35229, 35230, 35231, 35277, 35103, 35145, 35187})
	// @formatter:on
	public void onNpcManorBypass(OnNpcManorBypass evt)
	{
		final PlayerInstance player = evt.getActiveChar();
		if (CastleManorManager.getInstance().isUnderMaintenance())
		{
			player.sendPacket(SystemMessageId.THE_MANOR_SYSTEM_IS_CURRENTLY_UNDER_MAINTENANCE);
			return;
		}
		
		final Npc npc = evt.getTarget();
		final int templateId = npc.getParameters().getInt("manor_id", -1);
		final int castleId = (evt.getManorId() == -1) ? templateId : evt.getManorId();
		switch (evt.getRequest())
		{
			case 1: // Seed purchase
			{
				if (templateId != castleId)
				{
					player.sendPacket(new SystemMessage(SystemMessageId.HERE_YOU_CAN_BUY_ONLY_SEEDS_OF_S1_MANOR).addCastleId(templateId));
					return;
				}
				player.sendPacket(new BuyListSeed(player.getAdena(), castleId));
				break;
			}
			case 2: // Crop sales
			{
				player.sendPacket(new ExShowSellCropList(player.getInventory(), castleId));
				break;
			}
			case 3: // Seed info
			{
				player.sendPacket(new ExShowSeedInfo(castleId, evt.isNextPeriod(), false));
				break;
			}
			case 4: // Crop info
			{
				player.sendPacket(new ExShowCropInfo(castleId, evt.isNextPeriod(), false));
				break;
			}
			case 5: // Basic info
			{
				player.sendPacket(new ExShowManorDefaultInfo(false));
				break;
			}
			case 6: // Buy harvester
			{
				((MerchantInstance) npc).showBuyWindow(player, 300000 + npc.getId());
				break;
			}
			case 9: // Edit sales (Crop sales)
			{
				player.sendPacket(new ExShowProcureCropDetail(evt.getManorId()));
				break;
			}
			default:
			{
				LOGGER.warning(getClass().getSimpleName() + ": Player " + player.getName() + " (" + player.getObjectId() + ") send unknown request id " + evt.getRequest() + "!");
			}
		}
	}
	
	public static void main(String[] args)
	{
		new ManorManager();
	}
}