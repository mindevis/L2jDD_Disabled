
package org.l2jdd.gameserver.taskmanager;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.l2jdd.Config;
import org.l2jdd.commons.concurrent.ThreadPool;
import org.l2jdd.gameserver.handler.ItemHandler;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.model.items.instance.ItemInstance;

/**
 * @author Mobius, Gigi
 */
public class AutoPotionTaskManager
{
	private static final Set<PlayerInstance> PLAYERS = ConcurrentHashMap.newKeySet();
	private static boolean _working = false;
	
	public AutoPotionTaskManager()
	{
		ThreadPool.scheduleAtFixedRate(() ->
		{
			if (_working)
			{
				return;
			}
			_working = true;
			
			PLAYER: for (PlayerInstance player : PLAYERS)
			{
				if ((player == null) || player.isAlikeDead() || (player.isOnlineInt() != 1) || (!Config.AUTO_POTIONS_IN_OLYMPIAD && player.isInOlympiadMode()))
				{
					remove(player);
					continue PLAYER;
				}
				
				boolean success = false;
				if (Config.AUTO_HP_ENABLED)
				{
					final boolean restoreHP = ((player.getStatus().getCurrentHp() / player.getMaxHp()) * 100) < Config.AUTO_HP_PERCENTAGE;
					HP: for (int itemId : Config.AUTO_HP_ITEM_IDS)
					{
						final ItemInstance hpPotion = player.getInventory().getItemByItemId(itemId);
						if ((hpPotion != null) && (hpPotion.getCount() > 0))
						{
							success = true;
							if (restoreHP)
							{
								ItemHandler.getInstance().getHandler(hpPotion.getEtcItem()).useItem(player, hpPotion, false);
								player.sendMessage("Auto potion: Restored HP.");
								break HP;
							}
						}
					}
				}
				if (Config.AUTO_CP_ENABLED)
				{
					final boolean restoreCP = ((player.getStatus().getCurrentCp() / player.getMaxCp()) * 100) < Config.AUTO_CP_PERCENTAGE;
					CP: for (int itemId : Config.AUTO_CP_ITEM_IDS)
					{
						final ItemInstance cpPotion = player.getInventory().getItemByItemId(itemId);
						if ((cpPotion != null) && (cpPotion.getCount() > 0))
						{
							success = true;
							if (restoreCP)
							{
								ItemHandler.getInstance().getHandler(cpPotion.getEtcItem()).useItem(player, cpPotion, false);
								player.sendMessage("Auto potion: Restored CP.");
								break CP;
							}
						}
					}
				}
				if (Config.AUTO_MP_ENABLED)
				{
					final boolean restoreMP = ((player.getStatus().getCurrentMp() / player.getMaxMp()) * 100) < Config.AUTO_MP_PERCENTAGE;
					MP: for (int itemId : Config.AUTO_MP_ITEM_IDS)
					{
						final ItemInstance mpPotion = player.getInventory().getItemByItemId(itemId);
						if ((mpPotion != null) && (mpPotion.getCount() > 0))
						{
							success = true;
							if (restoreMP)
							{
								ItemHandler.getInstance().getHandler(mpPotion.getEtcItem()).useItem(player, mpPotion, false);
								player.sendMessage("Auto potion: Restored MP.");
								break MP;
							}
						}
					}
				}
				
				if (!success)
				{
					player.sendMessage("Auto potion: You are out of potions!");
				}
			}
			
			_working = false;
		}, 0, 1000);
	}
	
	public void add(PlayerInstance player)
	{
		if (!PLAYERS.contains(player))
		{
			PLAYERS.add(player);
		}
	}
	
	public void remove(PlayerInstance player)
	{
		PLAYERS.remove(player);
	}
	
	public static AutoPotionTaskManager getInstance()
	{
		return SingletonHolder.INSTANCE;
	}
	
	private static class SingletonHolder
	{
		protected static final AutoPotionTaskManager INSTANCE = new AutoPotionTaskManager();
	}
}
