
package org.l2jdd.gameserver.model;

import org.l2jdd.Config;
import org.l2jdd.gameserver.data.ItemTable;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.model.items.instance.ItemInstance;
import org.l2jdd.gameserver.network.SystemMessageId;
import org.l2jdd.gameserver.network.serverpackets.InventoryUpdate;
import org.l2jdd.gameserver.network.serverpackets.SystemMessage;

public class CombatFlag
{
	private PlayerInstance _player = null;
	private int _playerId = 0;
	private ItemInstance _item = null;
	private ItemInstance _itemInstance;
	private final Location _location;
	private final int _itemId;
	@SuppressWarnings("unused")
	private final int _fortId;
	
	public CombatFlag(int fortId, int x, int y, int z, int heading, int itemId)
	{
		_fortId = fortId;
		_location = new Location(x, y, z, heading);
		_itemId = itemId;
	}
	
	public synchronized void spawnMe()
	{
		// Init the dropped ItemInstance and add it in the world as a visible object at the position where mob was last
		_itemInstance = ItemTable.getInstance().createItem("Combat", _itemId, 1, null, null);
		_itemInstance.dropMe(null, _location.getX(), _location.getY(), _location.getZ());
	}
	
	public synchronized void unSpawnMe()
	{
		if (_player != null)
		{
			dropIt();
		}
		if (_itemInstance != null)
		{
			_itemInstance.decayMe();
		}
	}
	
	public boolean activate(PlayerInstance player, ItemInstance item)
	{
		if (player.isMounted())
		{
			player.sendPacket(SystemMessageId.YOU_DO_NOT_MEET_THE_REQUIRED_CONDITION_TO_EQUIP_THAT_ITEM);
			return false;
		}
		
		// Player holding it data
		_player = player;
		_playerId = _player.getObjectId();
		_itemInstance = null;
		
		// Equip with the weapon
		_item = item;
		_player.getInventory().equipItem(_item);
		final SystemMessage sm = new SystemMessage(SystemMessageId.YOU_HAVE_EQUIPPED_YOUR_S1);
		sm.addItemName(_item);
		_player.sendPacket(sm);
		
		// Refresh inventory
		if (!Config.FORCE_INVENTORY_UPDATE)
		{
			final InventoryUpdate iu = new InventoryUpdate();
			iu.addItem(_item);
			_player.sendInventoryUpdate(iu);
		}
		else
		{
			_player.sendItemList();
		}
		// Refresh player stats
		_player.broadcastUserInfo();
		_player.setCombatFlagEquipped(true);
		return true;
	}
	
	public void dropIt()
	{
		// Reset player stats
		_player.setCombatFlagEquipped(false);
		final long slot = _player.getInventory().getSlotFromItem(_item);
		_player.getInventory().unEquipItemInBodySlot(slot);
		_player.destroyItem("CombatFlag", _item, null, true);
		_item = null;
		_player.broadcastUserInfo();
		_player = null;
		_playerId = 0;
	}
	
	public int getPlayerObjectId()
	{
		return _playerId;
	}
	
	public ItemInstance getCombatFlagInstance()
	{
		return _itemInstance;
	}
}
