
package org.l2jdd.gameserver.network.serverpackets;

import org.l2jdd.gameserver.model.interfaces.IUpdateTypeComponent;

/**
 * @author UnAfraid
 * @param <T>
 */
public abstract class AbstractMaskPacket<T extends IUpdateTypeComponent> implements IClientOutgoingPacket
{
	protected static final byte[] DEFAULT_FLAG_ARRAY =
	{
		(byte) 0x80,
		0x40,
		0x20,
		0x10,
		0x08,
		0x04,
		0x02,
		0x01
	};
	
	protected abstract byte[] getMasks();
	
	protected void onNewMaskAdded(T component)
	{
	}
	
	@SafeVarargs
	public final void addComponentType(T... updateComponents)
	{
		for (T component : updateComponents)
		{
			if (!containsMask(component))
			{
				addMask(component.getMask());
				onNewMaskAdded(component);
			}
		}
	}
	
	protected void addMask(int mask)
	{
		getMasks()[mask >> 3] |= DEFAULT_FLAG_ARRAY[mask & 7];
	}
	
	public boolean containsMask(T component)
	{
		return containsMask(component.getMask());
	}
	
	public boolean containsMask(int mask)
	{
		return (getMasks()[mask >> 3] & DEFAULT_FLAG_ARRAY[mask & 7]) != 0;
	}
	
	/**
	 * @param masks
	 * @param type
	 * @return {@code true} if the mask contains the current update component type
	 */
	public boolean containsMask(int masks, T type)
	{
		return (masks & type.getMask()) == type.getMask();
	}
}
