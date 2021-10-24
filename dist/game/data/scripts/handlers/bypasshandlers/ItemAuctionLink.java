
package handlers.bypasshandlers;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.StringTokenizer;
import java.util.logging.Level;

import org.l2jdd.Config;
import org.l2jdd.gameserver.handler.IBypassHandler;
import org.l2jdd.gameserver.instancemanager.ItemAuctionManager;
import org.l2jdd.gameserver.model.actor.Creature;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.model.itemauction.ItemAuction;
import org.l2jdd.gameserver.model.itemauction.ItemAuctionInstance;
import org.l2jdd.gameserver.network.SystemMessageId;
import org.l2jdd.gameserver.network.serverpackets.ExItemAuctionInfoPacket;

public class ItemAuctionLink implements IBypassHandler
{
	private static final SimpleDateFormat SDF = new SimpleDateFormat("HH:mm:ss dd.MM.yyyy");
	
	private static final String[] COMMANDS =
	{
		"ItemAuction"
	};
	
	@Override
	public boolean useBypass(String command, PlayerInstance player, Creature target)
	{
		if (!target.isNpc())
		{
			return false;
		}
		
		if (!Config.ALT_ITEM_AUCTION_ENABLED)
		{
			player.sendPacket(SystemMessageId.IT_IS_NOT_AN_AUCTION_PERIOD);
			return true;
		}
		
		final ItemAuctionInstance au = ItemAuctionManager.getInstance().getManagerInstance(target.getId());
		if (au == null)
		{
			return false;
		}
		
		try
		{
			final StringTokenizer st = new StringTokenizer(command);
			st.nextToken(); // bypass "ItemAuction"
			if (!st.hasMoreTokens())
			{
				return false;
			}
			
			final String cmd = st.nextToken();
			if ("show".equalsIgnoreCase(cmd))
			{
				if (!player.getFloodProtectors().getItemAuction().tryPerformAction("RequestInfoItemAuction"))
				{
					return false;
				}
				
				if (player.isItemAuctionPolling())
				{
					return false;
				}
				
				final ItemAuction currentAuction = au.getCurrentAuction();
				final ItemAuction nextAuction = au.getNextAuction();
				if (currentAuction == null)
				{
					player.sendPacket(SystemMessageId.IT_IS_NOT_AN_AUCTION_PERIOD);
					
					if (nextAuction != null)
					{
						player.sendMessage("The next auction will begin on the " + SDF.format(new Date(nextAuction.getStartingTime())) + ".");
					}
					return true;
				}
				
				player.sendPacket(new ExItemAuctionInfoPacket(false, currentAuction, nextAuction));
			}
			else if ("cancel".equalsIgnoreCase(cmd))
			{
				final ItemAuction[] auctions = au.getAuctionsByBidder(player.getObjectId());
				boolean returned = false;
				for (ItemAuction auction : auctions)
				{
					if (auction.cancelBid(player))
					{
						returned = true;
					}
				}
				if (!returned)
				{
					player.sendPacket(SystemMessageId.THERE_ARE_NO_OFFERINGS_I_OWN_OR_I_MADE_A_BID_FOR);
				}
			}
			else
			{
				return false;
			}
		}
		catch (Exception e)
		{
			LOGGER.log(Level.WARNING, "Exception in " + getClass().getSimpleName(), e);
		}
		
		return true;
	}
	
	@Override
	public String[] getBypassList()
	{
		return COMMANDS;
	}
}
