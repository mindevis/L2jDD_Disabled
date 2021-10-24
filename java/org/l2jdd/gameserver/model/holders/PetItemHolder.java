
package org.l2jdd.gameserver.model.holders;

import org.l2jdd.gameserver.model.items.instance.ItemInstance;

/**
 * @author UnAfraid
 */
public class PetItemHolder
{
	private final ItemInstance _item;
	
	public PetItemHolder(ItemInstance item)
	{
		_item = item;
	}
	
	public ItemInstance getItem()
	{
		return _item;
	}
}
