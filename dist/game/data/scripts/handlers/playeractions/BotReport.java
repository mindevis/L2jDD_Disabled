
package handlers.playeractions;

import org.l2jdd.Config;
import org.l2jdd.gameserver.data.BotReportTable;
import org.l2jdd.gameserver.handler.IPlayerActionHandler;
import org.l2jdd.gameserver.model.ActionDataHolder;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;

/**
 * Bot Report button player action handler.
 * @author Nik
 */
public class BotReport implements IPlayerActionHandler
{
	@Override
	public void useAction(PlayerInstance player, ActionDataHolder data, boolean ctrlPressed, boolean shiftPressed)
	{
		if (Config.BOTREPORT_ENABLE)
		{
			BotReportTable.getInstance().reportBot(player);
		}
		else
		{
			player.sendMessage("This feature is disabled.");
		}
	}
}
