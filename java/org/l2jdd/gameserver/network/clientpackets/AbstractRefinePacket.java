
package org.l2jdd.gameserver.network.clientpackets;

import java.util.Arrays;

import org.l2jdd.Config;
import org.l2jdd.gameserver.enums.ItemLocation;
import org.l2jdd.gameserver.enums.PrivateStoreType;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.model.actor.request.EnchantItemAttributeRequest;
import org.l2jdd.gameserver.model.actor.request.EnchantItemRequest;
import org.l2jdd.gameserver.model.items.Armor;
import org.l2jdd.gameserver.model.items.Weapon;
import org.l2jdd.gameserver.model.items.instance.ItemInstance;
import org.l2jdd.gameserver.model.options.VariationFee;
import org.l2jdd.gameserver.model.skills.AbnormalType;
import org.l2jdd.gameserver.network.SystemMessageId;

public abstract class AbstractRefinePacket implements IClientIncomingPacket
{
	/**
	 * Checks player, source item, lifestone and gemstone validity for augmentation process
	 * @param player
	 * @param item
	 * @param mineralItem
	 * @param feeItem
	 * @param fee
	 * @return
	 */
	protected static boolean isValid(PlayerInstance player, ItemInstance item, ItemInstance mineralItem, ItemInstance feeItem, VariationFee fee)
	{
		if (fee == null)
		{
			return false;
		}
		
		if (!isValid(player, item, mineralItem))
		{
			return false;
		}
		
		// GemStones must belong to owner
		if (feeItem.getOwnerId() != player.getObjectId())
		{
			return false;
		}
		// .. and located in inventory
		if (feeItem.getItemLocation() != ItemLocation.INVENTORY)
		{
			return false;
		}
		
		// TODO: Update XMLs.
		// Check for item id
		// if (fee.getItemId() != feeItem.getId())
		// {
		// return false;
		// }
		
		// TODO: Update XMLs.
		// Count must be greater or equal of required number
		// if (fee.getItemCount() > feeItem.getCount())
		// {
		// return false;
		// }
		
		return true;
	}
	
	/**
	 * Checks player, source item and lifestone validity for augmentation process
	 * @param player
	 * @param item
	 * @param mineralItem
	 * @return
	 */
	protected static boolean isValid(PlayerInstance player, ItemInstance item, ItemInstance mineralItem)
	{
		if (!isValid(player, item))
		{
			return false;
		}
		
		// Item must belong to owner
		if (mineralItem.getOwnerId() != player.getObjectId())
		{
			return false;
		}
		// Lifestone must be located in inventory
		if (mineralItem.getItemLocation() != ItemLocation.INVENTORY)
		{
			return false;
		}
		
		return true;
	}
	
	/**
	 * Check both player and source item conditions for augmentation process
	 * @param player
	 * @param item
	 * @return
	 */
	protected static boolean isValid(PlayerInstance player, ItemInstance item)
	{
		if (!isValid(player))
		{
			return false;
		}
		
		// Item must belong to owner
		if (item.getOwnerId() != player.getObjectId())
		{
			return false;
		}
		// Remove the augmentation if any (286).
		// if (item.isAugmented())
		// {
		// return false;
		// }
		if (item.isHeroItem())
		{
			return false;
		}
		if (item.isShadowItem())
		{
			return false;
		}
		if (item.isCommonItem())
		{
			return false;
		}
		if (item.isEtcItem())
		{
			return false;
		}
		if (item.isTimeLimitedItem())
		{
			return false;
		}
		if (item.isPvp() && !Config.ALT_ALLOW_AUGMENT_PVP_ITEMS)
		{
			return false;
		}
		
		// Source item can be equipped or in inventory
		switch (item.getItemLocation())
		{
			case INVENTORY:
			case PAPERDOLL:
			{
				break;
			}
			default:
			{
				return false;
			}
		}
		
		if (!(item.getItem() instanceof Weapon) && !(item.getItem() instanceof Armor))
		{
			return false; // neither weapon nor armor ?
		}
		
		// blacklist check
		if (Arrays.binarySearch(Config.AUGMENTATION_BLACKLIST, item.getId()) >= 0)
		{
			return false;
		}
		
		return true;
	}
	
	/**
	 * Check if player's conditions valid for augmentation process
	 * @param player
	 * @return
	 */
	protected static boolean isValid(PlayerInstance player)
	{
		if (player.getPrivateStoreType() != PrivateStoreType.NONE)
		{
			player.sendPacket(SystemMessageId.YOU_CANNOT_AUGMENT_ITEMS_WHILE_A_PRIVATE_STORE_OR_PRIVATE_WORKSHOP_IS_IN_OPERATION);
			return false;
		}
		if (player.getActiveTradeList() != null)
		{
			player.sendPacket(SystemMessageId.YOU_CANNOT_AUGMENT_ITEMS_WHILE_ENGAGED_IN_TRADE_ACTIVITIES);
			return false;
		}
		if (player.isDead())
		{
			player.sendPacket(SystemMessageId.YOU_CANNOT_AUGMENT_ITEMS_WHILE_DEAD);
			return false;
		}
		if (player.hasBlockActions() && player.hasAbnormalType(AbnormalType.PARALYZE))
		{
			player.sendPacket(SystemMessageId.YOU_CANNOT_AUGMENT_ITEMS_WHILE_PARALYZED);
			return false;
		}
		if (player.isFishing())
		{
			player.sendPacket(SystemMessageId.YOU_CANNOT_AUGMENT_ITEMS_WHILE_FISHING);
			return false;
		}
		if (player.isSitting())
		{
			player.sendPacket(SystemMessageId.YOU_CANNOT_AUGMENT_ITEMS_WHILE_SITTING_DOWN);
			return false;
		}
		if (player.isCursedWeaponEquipped())
		{
			return false;
		}
		if (player.hasRequest(EnchantItemRequest.class, EnchantItemAttributeRequest.class) || player.isProcessingTransaction())
		{
			return false;
		}
		return true;
	}
}
