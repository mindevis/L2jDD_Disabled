
package handlers.itemhandlers;

import org.l2jdd.gameserver.model.actor.Playable;
import org.l2jdd.gameserver.model.items.instance.ItemInstance;
import org.l2jdd.gameserver.network.SystemMessageId;

public class Elixir extends ItemSkills
{
	@Override
	public boolean useItem(Playable playable, ItemInstance item, boolean forceUse)
	{
		if (!playable.isPlayer())
		{
			playable.sendPacket(SystemMessageId.YOUR_PET_CANNOT_CARRY_THIS_ITEM);
			return false;
		}
		return super.useItem(playable, item, forceUse);
	}
}
