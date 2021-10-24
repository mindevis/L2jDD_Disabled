
package org.l2jdd.gameserver.model.eventengine.drop;

import java.util.Collection;

import org.l2jdd.gameserver.model.holders.ItemHolder;

/**
 * @author UnAfraid
 */
public interface IEventDrop
{
	Collection<ItemHolder> calculateDrops();
}
