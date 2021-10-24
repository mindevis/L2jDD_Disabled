
package org.l2jdd.gameserver.util;

/**
 * @author HorridoJoho
 * @param <E> The enum type
 */
public class EnumIntBitmask<E extends Enum<E>> implements Cloneable
{
	public static <E extends Enum<E>> int getAllBitmask(Class<E> enumClass)
	{
		int allBitmask = 0;
		final E[] values = enumClass.getEnumConstants();
		if (values.length > 32)
		{
			throw new IllegalArgumentException("Enum too big for an integer bitmask.");
		}
		for (E value : values)
		{
			allBitmask |= 1 << value.ordinal();
		}
		return allBitmask;
	}
	
	private final Class<E> _enumClass;
	private int _bitmask;
	
	public EnumIntBitmask(Class<E> enumClass, boolean all)
	{
		_enumClass = enumClass;
		
		final E[] values = _enumClass.getEnumConstants();
		if (values.length > 32)
		{
			throw new IllegalArgumentException("Enum too big for an integer bitmask.");
		}
		
		if (all)
		{
			setAll();
		}
		else
		{
			clear();
		}
	}
	
	public EnumIntBitmask(Class<E> enumClass, int bitmask)
	{
		_enumClass = enumClass;
		_bitmask = bitmask;
	}
	
	public void setAll()
	{
		set(_enumClass.getEnumConstants());
	}
	
	public void clear()
	{
		_bitmask = 0;
	}
	
	@SafeVarargs
	public final void set(E... many)
	{
		clear();
		for (E one : many)
		{
			_bitmask |= 1 << one.ordinal();
		}
	}
	
	@SafeVarargs
	public final void set(E first, E... more)
	{
		clear();
		add(first, more);
	}
	
	public void setBitmask(int bitmask)
	{
		_bitmask = bitmask;
	}
	
	@SafeVarargs
	public final void add(E first, E... more)
	{
		_bitmask |= 1 << first.ordinal();
		if (more != null)
		{
			for (E one : more)
			{
				_bitmask |= 1 << one.ordinal();
			}
		}
	}
	
	@SafeVarargs
	public final void remove(E first, E... more)
	{
		_bitmask &= ~(1 << first.ordinal());
		if (more != null)
		{
			for (E one : more)
			{
				_bitmask &= ~(1 << one.ordinal());
			}
		}
	}
	
	@SafeVarargs
	public final boolean has(E first, E... more)
	{
		if ((_bitmask & (1 << first.ordinal())) == 0)
		{
			return false;
		}
		
		for (E one : more)
		{
			if ((_bitmask & (1 << one.ordinal())) == 0)
			{
				return false;
			}
		}
		return true;
	}
	
	@Override
	public EnumIntBitmask<E> clone()
	{
		return new EnumIntBitmask<>(_enumClass, _bitmask);
	}
	
	public int getBitmask()
	{
		return _bitmask;
	}
}
