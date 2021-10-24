
package org.l2jdd.gameserver.model.itemcontainer;

import org.l2jdd.gameserver.enums.ItemLocation;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;

public class PlayerWarehouse extends Warehouse
{
	private final PlayerInstance _owner;
	
	public PlayerWarehouse(PlayerInstance owner)
	{
		_owner = owner;
	}
	
	@Override
	public String getName()
	{
		return "Warehouse";
	}
	
	@Override
	public PlayerInstance getOwner()
	{
		return _owner;
	}
	
	@Override
	public ItemLocation getBaseLocation()
	{
		return ItemLocation.WAREHOUSE;
	}
	
	@Override
	public boolean validateCapacity(long slots)
	{
		return ((_items.size() + slots) <= _owner.getWareHouseLimit());
	}
}
