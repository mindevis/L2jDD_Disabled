
package handlers.itemhandlers;

import org.l2jdd.gameserver.handler.IItemHandler;
import org.l2jdd.gameserver.model.actor.Playable;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.model.items.instance.ItemInstance;
import org.l2jdd.gameserver.network.SystemMessageId;
import org.l2jdd.gameserver.network.serverpackets.EtcStatusUpdate;
import org.l2jdd.gameserver.network.serverpackets.SystemMessage;

/**
 * Charm Of Courage Handler
 * @author Zealar
 */
public class CharmOfCourage implements IItemHandler
{
	@Override
	public boolean useItem(Playable playable, ItemInstance item, boolean forceUse)
	{
		if (!playable.isPlayer())
		{
			return false;
		}
		
		final PlayerInstance player = playable.getActingPlayer();
		int level = player.getLevel();
		final int itemLevel = item.getItem().getCrystalType().getLevel();
		if (level < 20)
		{
			level = 0;
		}
		else if (level < 40)
		{
			level = 1;
		}
		else if (level < 52)
		{
			level = 2;
		}
		else if (level < 61)
		{
			level = 3;
		}
		else if (level < 76)
		{
			level = 4;
		}
		else
		{
			level = 5;
		}
		
		if (itemLevel < level)
		{
			final SystemMessage sm = new SystemMessage(SystemMessageId.S1_CANNOT_BE_USED_DUE_TO_UNSUITABLE_TERMS);
			sm.addItemName(item.getId());
			player.sendPacket(sm);
			return false;
		}
		
		if (player.destroyItemWithoutTrace("Consume", item.getObjectId(), 1, null, false))
		{
			player.setCharmOfCourage(true);
			player.sendPacket(new EtcStatusUpdate(player));
			return true;
		}
		return false;
	}
}
