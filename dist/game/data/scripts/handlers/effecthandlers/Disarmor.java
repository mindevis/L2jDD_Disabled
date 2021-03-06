
package handlers.effecthandlers;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.l2jdd.gameserver.data.ItemTable;
import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.actor.Creature;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.model.effects.AbstractEffect;
import org.l2jdd.gameserver.model.items.Item;
import org.l2jdd.gameserver.model.items.instance.ItemInstance;
import org.l2jdd.gameserver.model.skills.Skill;
import org.l2jdd.gameserver.network.SystemMessageId;
import org.l2jdd.gameserver.network.serverpackets.InventoryUpdate;
import org.l2jdd.gameserver.network.serverpackets.SystemMessage;

/**
 * Disarm by inventory slot effect implementation. At end of effect, it re-equips that item.
 * @author Nik
 */
public class Disarmor extends AbstractEffect
{
	private final Map<Integer, Integer> _unequippedItems; // PlayerObjId, ItemObjId
	private final long _slot;
	
	public Disarmor(StatSet params)
	{
		_unequippedItems = new ConcurrentHashMap<>();
		
		final String slot = params.getString("slot", "chest");
		_slot = ItemTable.SLOTS.getOrDefault(slot, (long) Item.SLOT_NONE);
		if (_slot == Item.SLOT_NONE)
		{
			LOGGER.severe("Unknown bodypart slot for effect: " + slot);
		}
	}
	
	@Override
	public boolean canStart(Creature effector, Creature effected, Skill skill)
	{
		return (_slot != Item.SLOT_NONE) && effected.isPlayer();
	}
	
	@Override
	public void continuousInstant(Creature effector, Creature effected, Skill skill, ItemInstance item)
	{
		if (!effected.isPlayer())
		{
			return;
		}
		
		final PlayerInstance player = effected.getActingPlayer();
		final ItemInstance[] unequiped = player.getInventory().unEquipItemInBodySlotAndRecord(_slot);
		if (unequiped.length > 0)
		{
			final InventoryUpdate iu = new InventoryUpdate();
			for (ItemInstance itm : unequiped)
			{
				iu.addModifiedItem(itm);
			}
			player.sendInventoryUpdate(iu);
			player.broadcastUserInfo();
			
			SystemMessage sm = null;
			if (unequiped[0].getEnchantLevel() > 0)
			{
				sm = new SystemMessage(SystemMessageId.S1_S2_HAS_BEEN_UNEQUIPPED);
				sm.addInt(unequiped[0].getEnchantLevel());
				sm.addItemName(unequiped[0]);
			}
			else
			{
				sm = new SystemMessage(SystemMessageId.S1_HAS_BEEN_UNEQUIPPED);
				sm.addItemName(unequiped[0]);
			}
			player.sendPacket(sm);
			effected.getInventory().blockItemSlot(_slot);
			_unequippedItems.put(effected.getObjectId(), unequiped[0].getObjectId());
		}
	}
	
	@Override
	public void onExit(Creature effector, Creature effected, Skill skill)
	{
		if (!effected.isPlayer())
		{
			return;
		}
		
		final Integer disarmedObjId = _unequippedItems.remove(effected.getObjectId());
		if ((disarmedObjId != null) && (disarmedObjId > 0))
		{
			final PlayerInstance player = effected.getActingPlayer();
			player.getInventory().unblockItemSlot(_slot);
			
			final ItemInstance item = player.getInventory().getItemByObjectId(disarmedObjId);
			if (item != null)
			{
				player.getInventory().equipItem(item);
				final InventoryUpdate iu = new InventoryUpdate();
				iu.addModifiedItem(item);
				player.sendInventoryUpdate(iu);
				
				SystemMessage sm = null;
				if (item.isEquipped())
				{
					if (item.getEnchantLevel() > 0)
					{
						sm = new SystemMessage(SystemMessageId.EQUIPPED_S1_S2);
						sm.addInt(item.getEnchantLevel());
						sm.addItemName(item);
					}
					else
					{
						sm = new SystemMessage(SystemMessageId.YOU_HAVE_EQUIPPED_YOUR_S1);
						sm.addItemName(item);
					}
					player.sendPacket(sm);
				}
			}
		}
	}
}
