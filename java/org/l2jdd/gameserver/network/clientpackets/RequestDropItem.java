
package org.l2jdd.gameserver.network.clientpackets;

import org.l2jdd.Config;
import org.l2jdd.commons.network.PacketReader;
import org.l2jdd.gameserver.data.xml.AdminData;
import org.l2jdd.gameserver.enums.PrivateStoreType;
import org.l2jdd.gameserver.model.PlayerCondOverride;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.model.holders.SkillUseHolder;
import org.l2jdd.gameserver.model.itemcontainer.Inventory;
import org.l2jdd.gameserver.model.items.Item;
import org.l2jdd.gameserver.model.items.instance.ItemInstance;
import org.l2jdd.gameserver.model.items.type.ActionType;
import org.l2jdd.gameserver.model.items.type.EtcItemType;
import org.l2jdd.gameserver.model.skills.Skill;
import org.l2jdd.gameserver.model.skills.SkillCaster;
import org.l2jdd.gameserver.model.zone.ZoneId;
import org.l2jdd.gameserver.network.GameClient;
import org.l2jdd.gameserver.network.SystemMessageId;
import org.l2jdd.gameserver.util.GMAudit;
import org.l2jdd.gameserver.util.Util;

/**
 * @version $Revision: 1.11.2.1.2.7 $ $Date: 2005/04/02 21:25:21 $
 */
public class RequestDropItem implements IClientIncomingPacket
{
	private int _objectId;
	private long _count;
	private int _x;
	private int _y;
	private int _z;
	
	@Override
	public boolean read(GameClient client, PacketReader packet)
	{
		_objectId = packet.readD();
		_count = packet.readQ();
		_x = packet.readD();
		_y = packet.readD();
		_z = packet.readD();
		return true;
	}
	
	@Override
	public void run(GameClient client)
	{
		final PlayerInstance player = client.getPlayer();
		if ((player == null) || player.isDead())
		{
			return;
		}
		
		// Flood protect drop to avoid packet lag
		if (!client.getFloodProtectors().getDropItem().tryPerformAction("drop item"))
		{
			return;
		}
		
		final ItemInstance item = player.getInventory().getItemByObjectId(_objectId);
		if ((item == null) || (_count == 0) || !player.validateItemManipulation(_objectId, "drop") || (!Config.ALLOW_DISCARDITEM && !player.canOverrideCond(PlayerCondOverride.DROP_ALL_ITEMS)) || (!item.isDropable() && !(player.canOverrideCond(PlayerCondOverride.DROP_ALL_ITEMS) && Config.GM_TRADE_RESTRICTED_ITEMS)) || ((item.getItemType() == EtcItemType.PET_COLLAR) && player.havePetInvItems()) || player.isInsideZone(ZoneId.NO_ITEM_DROP))
		{
			player.sendPacket(SystemMessageId.THIS_ITEM_CANNOT_BE_DESTROYED);
			return;
		}
		
		if (item.isQuestItem() && !(player.canOverrideCond(PlayerCondOverride.DROP_ALL_ITEMS) && Config.GM_TRADE_RESTRICTED_ITEMS))
		{
			return;
		}
		
		if (_count > item.getCount())
		{
			player.sendPacket(SystemMessageId.THIS_ITEM_CANNOT_BE_DESTROYED);
			return;
		}
		
		if ((Config.PLAYER_SPAWN_PROTECTION > 0) && player.isInvul() && !player.isGM())
		{
			player.sendPacket(SystemMessageId.THIS_ITEM_CANNOT_BE_DESTROYED);
			return;
		}
		
		if (_count < 0)
		{
			Util.handleIllegalPlayerAction(player, "[RequestDropItem] Character " + player.getName() + " of account " + player.getAccountName() + " tried to drop item with oid " + _objectId + " but has count < 0!", Config.DEFAULT_PUNISH);
			return;
		}
		
		if (!item.isStackable() && (_count > 1))
		{
			Util.handleIllegalPlayerAction(player, "[RequestDropItem] Character " + player.getName() + " of account " + player.getAccountName() + " tried to drop non-stackable item with oid " + _objectId + " but has count > 1!", Config.DEFAULT_PUNISH);
			return;
		}
		
		if (Config.JAIL_DISABLE_TRANSACTION && player.isJailed())
		{
			player.sendMessage("You cannot drop items in Jail.");
			return;
		}
		
		if (!player.getAccessLevel().allowTransaction())
		{
			player.sendMessage("Transactions are disabled for your Access Level.");
			player.sendPacket(SystemMessageId.NOTHING_HAPPENED);
			return;
		}
		
		if (player.isProcessingTransaction() || (player.getPrivateStoreType() != PrivateStoreType.NONE))
		{
			player.sendPacket(SystemMessageId.WHILE_OPERATING_A_PRIVATE_STORE_OR_WORKSHOP_YOU_CANNOT_DISCARD_DESTROY_OR_TRADE_AN_ITEM);
			return;
		}
		
		if (player.isFishing())
		{
			// You can't mount, dismount, break and drop items while fishing
			player.sendPacket(SystemMessageId.YOU_CANNOT_DO_THAT_WHILE_FISHING_2);
			return;
		}
		
		if (player.isFlying())
		{
			return;
		}
		
		if (player.hasItemRequest())
		{
			player.sendPacket(SystemMessageId.YOU_CANNOT_DESTROY_OR_CRYSTALLIZE_ITEMS_WHILE_ENCHANTING_ATTRIBUTES);
			return;
		}
		
		// Cannot discard item that the skill is consuming.
		if (player.isCastingNow(s -> (s.getSkill().getItemConsumeId() == item.getId()) && (item.getItem().getDefaultAction() == ActionType.SKILL_REDUCE_ON_SKILL_SUCCESS)))
		{
			player.sendPacket(SystemMessageId.THIS_ITEM_CANNOT_BE_DESTROYED);
			return;
		}
		
		if ((Item.TYPE2_QUEST == item.getItem().getType2()) && !player.canOverrideCond(PlayerCondOverride.DROP_ALL_ITEMS))
		{
			player.sendPacket(SystemMessageId.THAT_ITEM_CANNOT_BE_DISCARDED_OR_EXCHANGED);
			return;
		}
		
		if (!player.isInsideRadius2D(_x, _y, 0, 150) || (Math.abs(_z - player.getZ()) > 50))
		{
			player.sendPacket(SystemMessageId.YOU_CANNOT_DISCARD_SOMETHING_THAT_FAR_AWAY_FROM_YOU);
			return;
		}
		
		if (!player.getInventory().canManipulateWithItemId(item.getId()))
		{
			player.sendMessage("You cannot use this item.");
			return;
		}
		
		// Do not drop items when casting known skills to avoid exploits.
		if (player.isCastingNow())
		{
			for (SkillCaster skillCaster : player.getSkillCasters())
			{
				final Skill skill = skillCaster.getSkill();
				if ((skill != null) && (player.getKnownSkill(skill.getId()) != null))
				{
					player.sendMessage("You cannot drop an item while casting " + skill.getName() + ".");
					return;
				}
			}
		}
		final SkillUseHolder skill = player.getQueuedSkill();
		if ((skill != null) && (player.getKnownSkill(skill.getSkillId()) != null))
		{
			player.sendMessage("You cannot drop an item while casting " + skill.getSkill().getName() + ".");
			return;
		}
		
		if (item.isEquipped())
		{
			player.getInventory().unEquipItemInSlot(item.getLocationSlot());
			player.broadcastUserInfo();
			player.sendItemList();
		}
		
		final ItemInstance dropedItem = player.dropItem("Drop", _objectId, _count, _x, _y, _z, null, false, false);
		
		// player.broadcastUserInfo();
		if (player.isGM())
		{
			final String target = (player.getTarget() != null ? player.getTarget().getName() : "no-target");
			GMAudit.auditGMAction(player.getName() + " [" + player.getObjectId() + "]", "Drop", target, "(id: " + dropedItem.getId() + " name: " + dropedItem.getItemName() + " objId: " + dropedItem.getObjectId() + " x: " + player.getX() + " y: " + player.getY() + " z: " + player.getZ() + ")");
		}
		
		if ((dropedItem != null) && (dropedItem.getId() == Inventory.ADENA_ID) && (dropedItem.getCount() >= 1000000))
		{
			final String msg = "Character (" + player.getName() + ") has dropped (" + dropedItem.getCount() + ")adena at (" + _x + "," + _y + "," + _z + ")";
			LOGGER.warning(msg);
			AdminData.getInstance().broadcastMessageToGMs(msg);
		}
	}
}
