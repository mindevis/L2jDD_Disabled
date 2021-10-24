
package handlers.itemhandlers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.l2jdd.gameserver.enums.ItemGrade;
import org.l2jdd.gameserver.enums.PrivateStoreType;
import org.l2jdd.gameserver.handler.IItemHandler;
import org.l2jdd.gameserver.model.ItemInfo;
import org.l2jdd.gameserver.model.actor.Playable;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.model.items.instance.ItemInstance;
import org.l2jdd.gameserver.network.SystemMessageId;
import org.l2jdd.gameserver.network.serverpackets.SystemMessage;
import org.l2jdd.gameserver.network.serverpackets.attributechange.ExChangeAttributeItemList;

/**
 * @author Mobius
 */
public class ChangeAttributeCrystal implements IItemHandler
{
	private static final Map<Integer, ItemGrade> ITEM_GRADES = new HashMap<>();
	static
	{
		ITEM_GRADES.put(33502, ItemGrade.S);
		ITEM_GRADES.put(35749, ItemGrade.R);
		ITEM_GRADES.put(45817, ItemGrade.R);
	}
	
	@Override
	public boolean useItem(Playable playable, ItemInstance item, boolean forceUse)
	{
		if (!playable.isPlayer())
		{
			playable.sendPacket(SystemMessageId.YOUR_PET_CANNOT_CARRY_THIS_ITEM);
			return false;
		}
		
		final PlayerInstance player = playable.getActingPlayer();
		if (player.getPrivateStoreType() != PrivateStoreType.NONE)
		{
			player.sendPacket(new SystemMessage(SystemMessageId.YOU_CANNOT_CHANGE_AN_ATTRIBUTE_WHILE_USING_A_PRIVATE_STORE_OR_WORKSHOP));
			return false;
		}
		
		if (ITEM_GRADES.get(item.getId()) == null)
		{
			player.sendPacket(new SystemMessage(SystemMessageId.CHANGING_ATTRIBUTES_HAS_BEEN_FAILED));
			return false;
		}
		
		final List<ItemInfo> itemList = new ArrayList<>();
		for (ItemInstance i : player.getInventory().getItems())
		{
			if (i.isWeapon() && i.hasAttributes() && (i.getItem().getItemGrade() == ITEM_GRADES.get(item.getId())))
			{
				itemList.add(new ItemInfo(i));
			}
		}
		
		if (itemList.isEmpty())
		{
			player.sendPacket(new SystemMessage(SystemMessageId.THE_ITEM_FOR_CHANGING_AN_ATTRIBUTE_DOES_NOT_EXIST));
			return false;
		}
		
		player.sendPacket(new ExChangeAttributeItemList(item.getId(), itemList.toArray(new ItemInfo[itemList.size()])));
		return true;
	}
}