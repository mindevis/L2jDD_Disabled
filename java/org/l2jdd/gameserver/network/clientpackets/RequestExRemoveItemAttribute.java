
package org.l2jdd.gameserver.network.clientpackets;

import org.l2jdd.commons.network.PacketReader;
import org.l2jdd.gameserver.enums.AttributeType;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.model.items.Weapon;
import org.l2jdd.gameserver.model.items.instance.ItemInstance;
import org.l2jdd.gameserver.network.GameClient;
import org.l2jdd.gameserver.network.SystemMessageId;
import org.l2jdd.gameserver.network.serverpackets.ExBaseAttributeCancelResult;
import org.l2jdd.gameserver.network.serverpackets.InventoryUpdate;
import org.l2jdd.gameserver.network.serverpackets.SystemMessage;
import org.l2jdd.gameserver.network.serverpackets.UserInfo;

public class RequestExRemoveItemAttribute implements IClientIncomingPacket
{
	private int _objectId;
	private long _price;
	private byte _element;
	
	@Override
	public boolean read(GameClient client, PacketReader packet)
	{
		_objectId = packet.readD();
		_element = (byte) packet.readD();
		return true;
	}
	
	@Override
	public void run(GameClient client)
	{
		final PlayerInstance player = client.getPlayer();
		if (player == null)
		{
			return;
		}
		
		final ItemInstance targetItem = player.getInventory().getItemByObjectId(_objectId);
		if (targetItem == null)
		{
			return;
		}
		
		final AttributeType type = AttributeType.findByClientId(_element);
		if (type == null)
		{
			return;
		}
		
		if ((targetItem.getAttributes() == null) || (targetItem.getAttribute(type) == null))
		{
			return;
		}
		
		if (player.reduceAdena("RemoveElement", getPrice(targetItem), player, true))
		{
			targetItem.clearAttribute(type);
			client.sendPacket(new UserInfo(player));
			
			final InventoryUpdate iu = new InventoryUpdate();
			iu.addModifiedItem(targetItem);
			player.sendInventoryUpdate(iu);
			SystemMessage sm;
			final AttributeType realElement = targetItem.isArmor() ? type.getOpposite() : type;
			if (targetItem.getEnchantLevel() > 0)
			{
				if (targetItem.isArmor())
				{
					sm = new SystemMessage(SystemMessageId.S3_ATTRIBUTE_WAS_REMOVED_FROM_S1_S2_SO_RESISTANCE_TO_S4_WAS_DECREASED);
				}
				else
				{
					sm = new SystemMessage(SystemMessageId.S1_S2_ATTRIBUTE_HAS_BEEN_REMOVED);
				}
				sm.addInt(targetItem.getEnchantLevel());
				sm.addItemName(targetItem);
				if (targetItem.isArmor())
				{
					sm.addAttribute(realElement.getClientId());
					sm.addAttribute(realElement.getOpposite().getClientId());
				}
			}
			else
			{
				if (targetItem.isArmor())
				{
					sm = new SystemMessage(SystemMessageId.S1_S_S2_ATTRIBUTE_WAS_REMOVED_AND_RESISTANCE_TO_S3_WAS_DECREASED);
				}
				else
				{
					sm = new SystemMessage(SystemMessageId.S1_S_S2_ATTRIBUTE_HAS_BEEN_REMOVED);
				}
				sm.addItemName(targetItem);
				if (targetItem.isArmor())
				{
					sm.addAttribute(realElement.getClientId());
					sm.addAttribute(realElement.getOpposite().getClientId());
				}
			}
			client.sendPacket(sm);
			client.sendPacket(new ExBaseAttributeCancelResult(targetItem.getObjectId(), _element));
		}
		else
		{
			client.sendPacket(SystemMessageId.YOU_DO_NOT_HAVE_ENOUGH_FUNDS_TO_CANCEL_THIS_ATTRIBUTE);
		}
	}
	
	private long getPrice(ItemInstance item)
	{
		switch (item.getItem().getCrystalType())
		{
			case S:
			{
				if (item.getItem() instanceof Weapon)
				{
					_price = 50000;
				}
				else
				{
					_price = 40000;
				}
				break;
			}
			case S80:
			{
				if (item.getItem() instanceof Weapon)
				{
					_price = 100000;
				}
				else
				{
					_price = 80000;
				}
				break;
			}
			case S84:
			{
				if (item.getItem() instanceof Weapon)
				{
					_price = 200000;
				}
				else
				{
					_price = 160000;
				}
				break;
			}
			case R:
			{
				if (item.getItem() instanceof Weapon)
				{
					_price = 400000;
				}
				else
				{
					_price = 320000;
				}
				break;
			}
			case R95:
			{
				if (item.getItem() instanceof Weapon)
				{
					_price = 800000;
				}
				else
				{
					_price = 640000;
				}
				break;
			}
			case R99:
			{
				if (item.getItem() instanceof Weapon)
				{
					_price = 3200000;
				}
				else
				{
					_price = 2560000;
				}
				break;
			}
			case R110:
			{
				if (item.getItem() instanceof Weapon)
				{
					_price = 6400000;
				}
				else
				{
					_price = 5120000;
				}
				break;
			}
		}
		return _price;
	}
}