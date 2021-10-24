/*
 * This file is part of the L2J Mobius project.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
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
