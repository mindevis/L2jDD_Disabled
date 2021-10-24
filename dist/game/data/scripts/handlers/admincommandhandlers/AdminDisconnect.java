
package handlers.admincommandhandlers;

import org.l2jdd.gameserver.handler.IAdminCommandHandler;
import org.l2jdd.gameserver.model.WorldObject;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.network.Disconnection;
import org.l2jdd.gameserver.util.BuilderUtil;

/**
 * This class handles following admin commands: - character_disconnect = disconnects target player
 * @version $Revision: 1.2.4.4 $ $Date: 2005/04/11 10:06:00 $
 */
public class AdminDisconnect implements IAdminCommandHandler
{
	private static final String[] ADMIN_COMMANDS =
	{
		"admin_character_disconnect"
	};
	
	@Override
	public boolean useAdminCommand(String command, PlayerInstance activeChar)
	{
		if (command.equals("admin_character_disconnect"))
		{
			disconnectCharacter(activeChar);
		}
		return true;
	}
	
	@Override
	public String[] getAdminCommandList()
	{
		return ADMIN_COMMANDS;
	}
	
	private void disconnectCharacter(PlayerInstance activeChar)
	{
		final WorldObject target = activeChar.getTarget();
		PlayerInstance player = null;
		if ((target != null) && target.isPlayer())
		{
			player = (PlayerInstance) target;
		}
		else
		{
			return;
		}
		
		if (player == activeChar)
		{
			BuilderUtil.sendSysMessage(activeChar, "You cannot logout your own character.");
		}
		else
		{
			BuilderUtil.sendSysMessage(activeChar, "Character " + player.getName() + " disconnected from server.");
			Disconnection.of(player).defaultSequence(false);
		}
	}
}
