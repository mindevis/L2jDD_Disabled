
package handlers.itemhandlers;

import org.l2jdd.gameserver.handler.IItemHandler;
import org.l2jdd.gameserver.model.actor.Playable;
import org.l2jdd.gameserver.model.items.instance.ItemInstance;
import org.l2jdd.gameserver.network.SystemMessageId;
import org.l2jdd.gameserver.network.serverpackets.ShowCalculator;

/**
 * @author Zoey76
 */
public class Calculator implements IItemHandler
{
	@Override
	public boolean useItem(Playable playable, ItemInstance item, boolean forceUse)
	{
		if (!playable.isPlayer())
		{
			playable.sendPacket(SystemMessageId.YOUR_PET_CANNOT_CARRY_THIS_ITEM);
			return false;
		}
		
		playable.sendPacket(new ShowCalculator(item.getId()));
		return true;
	}
}
