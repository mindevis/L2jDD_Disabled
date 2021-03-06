
package handlers.admincommandhandlers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.l2jdd.commons.database.DatabaseFactory;
import org.l2jdd.gameserver.data.xml.AdminData;
import org.l2jdd.gameserver.handler.IAdminCommandHandler;
import org.l2jdd.gameserver.model.AccessLevel;
import org.l2jdd.gameserver.model.World;
import org.l2jdd.gameserver.model.WorldObject;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.network.Disconnection;
import org.l2jdd.gameserver.network.SystemMessageId;
import org.l2jdd.gameserver.util.BuilderUtil;

/**
 * Change access level command handler.
 */
public class AdminChangeAccessLevel implements IAdminCommandHandler
{
	private static final String[] ADMIN_COMMANDS =
	{
		"admin_changelvl"
	};
	
	@Override
	public boolean useAdminCommand(String command, PlayerInstance activeChar)
	{
		final String[] parts = command.split(" ");
		if (parts.length == 2)
		{
			try
			{
				final int lvl = Integer.parseInt(parts[1]);
				final WorldObject target = activeChar.getTarget();
				if ((target == null) || !target.isPlayer())
				{
					activeChar.sendPacket(SystemMessageId.INVALID_TARGET);
				}
				else
				{
					onlineChange(activeChar, (PlayerInstance) target, lvl);
				}
			}
			catch (Exception e)
			{
				BuilderUtil.sendSysMessage(activeChar, "Usage: //changelvl <target_new_level> | <player_name> <new_level>");
			}
		}
		else if (parts.length == 3)
		{
			final String name = parts[1];
			final int lvl = Integer.parseInt(parts[2]);
			final PlayerInstance player = World.getInstance().getPlayer(name);
			if (player != null)
			{
				onlineChange(activeChar, player, lvl);
			}
			else
			{
				try (Connection con = DatabaseFactory.getConnection())
				{
					final PreparedStatement statement = con.prepareStatement("UPDATE characters SET accesslevel=? WHERE char_name=?");
					statement.setInt(1, lvl);
					statement.setString(2, name);
					statement.execute();
					final int count = statement.getUpdateCount();
					statement.close();
					if (count == 0)
					{
						BuilderUtil.sendSysMessage(activeChar, "Character not found or access level unaltered.");
					}
					else
					{
						BuilderUtil.sendSysMessage(activeChar, "Character's access level is now set to " + lvl);
					}
				}
				catch (SQLException se)
				{
					BuilderUtil.sendSysMessage(activeChar, "SQLException while changing character's access level");
				}
			}
		}
		return true;
	}
	
	@Override
	public String[] getAdminCommandList()
	{
		return ADMIN_COMMANDS;
	}
	
	/**
	 * @param activeChar the active GM
	 * @param player the online target
	 * @param lvl the access level
	 */
	private void onlineChange(PlayerInstance activeChar, PlayerInstance player, int lvl)
	{
		if (lvl >= 0)
		{
			final AccessLevel acccessLevel = AdminData.getInstance().getAccessLevel(lvl);
			if (acccessLevel != null)
			{
				player.setAccessLevel(lvl, true, true);
				player.sendMessage("Your access level has been changed to " + acccessLevel.getName() + " (" + acccessLevel.getLevel() + ").");
				activeChar.sendMessage(player.getName() + "'s access level has been changed to " + acccessLevel.getName() + " (" + acccessLevel.getLevel() + ").");
			}
			else
			{
				BuilderUtil.sendSysMessage(activeChar, "You are trying to set unexisting access level: " + lvl + " please try again with a valid one!");
			}
		}
		else
		{
			player.setAccessLevel(lvl, false, true);
			player.sendMessage("Your character has been banned. Bye.");
			Disconnection.of(player).defaultSequence(false);
		}
	}
}
