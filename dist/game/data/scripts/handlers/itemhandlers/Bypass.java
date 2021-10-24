
package handlers.itemhandlers;

import org.l2jdd.gameserver.cache.HtmCache;
import org.l2jdd.gameserver.handler.IItemHandler;
import org.l2jdd.gameserver.model.actor.Playable;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.model.items.instance.ItemInstance;
import org.l2jdd.gameserver.network.serverpackets.NpcHtmlMessage;

/**
 * @author JIV
 */
public class Bypass implements IItemHandler
{
	@Override
	public boolean useItem(Playable playable, ItemInstance item, boolean forceUse)
	{
		if (!playable.isPlayer())
		{
			return false;
		}
		final PlayerInstance player = (PlayerInstance) playable;
		final int itemId = item.getId();
		final String filename = "data/html/item/" + itemId + ".htm";
		final String content = HtmCache.getInstance().getHtm(player, filename);
		final NpcHtmlMessage html = new NpcHtmlMessage(0, item.getId());
		if (content == null)
		{
			html.setHtml("<html><body>My Text is missing:<br>" + filename + "</body></html>");
			player.sendPacket(html);
		}
		else
		{
			html.setHtml(content);
			html.replace("%itemId%", String.valueOf(item.getObjectId()));
			player.sendPacket(html);
		}
		return true;
	}
}
