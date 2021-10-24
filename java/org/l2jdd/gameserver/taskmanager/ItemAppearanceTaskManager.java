
package org.l2jdd.gameserver.taskmanager;

import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.l2jdd.commons.concurrent.ThreadPool;
import org.l2jdd.commons.util.Chronos;
import org.l2jdd.gameserver.model.items.instance.ItemInstance;

/**
 * @author Mobius
 */
public class ItemAppearanceTaskManager
{
	private static final Map<ItemInstance, Long> ITEMS = new ConcurrentHashMap<>();
	private static boolean _working = false;
	
	public ItemAppearanceTaskManager()
	{
		ThreadPool.scheduleAtFixedRate(() ->
		{
			if (_working)
			{
				return;
			}
			_working = true;
			
			final long currentTime = Chronos.currentTimeMillis();
			for (Entry<ItemInstance, Long> entry : ITEMS.entrySet())
			{
				if (currentTime > entry.getValue().longValue())
				{
					final ItemInstance item = entry.getKey();
					ITEMS.remove(item);
					item.onVisualLifeTimeEnd();
				}
			}
			
			_working = false;
		}, 1000, 1000);
	}
	
	public void add(ItemInstance item, long endTime)
	{
		if (!ITEMS.containsKey(item))
		{
			ITEMS.put(item, endTime);
		}
	}
	
	public void remove(ItemInstance item)
	{
		ITEMS.remove(item);
	}
	
	public static ItemAppearanceTaskManager getInstance()
	{
		return SingletonHolder.INSTANCE;
	}
	
	private static class SingletonHolder
	{
		protected static final ItemAppearanceTaskManager INSTANCE = new ItemAppearanceTaskManager();
	}
}
