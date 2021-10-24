
package handlers.admincommandhandlers;

import org.l2jdd.gameserver.handler.IAdminCommandHandler;
import org.l2jdd.gameserver.model.WorldObject;
import org.l2jdd.gameserver.model.actor.Creature;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.util.BuilderUtil;

/**
 * This class handles following admin commands: - invul = turns invulnerability on/off
 * @version $Revision: 1.2.4.4 $ $Date: 2007/07/31 10:06:02 $
 */
public class AdminInvul implements IAdminCommandHandler
{
	private static final String[] ADMIN_COMMANDS =
	{
		"admin_invul",
		"admin_setinvul",
		"admin_undying",
		"admin_setundying"
	};
	
	@Override
	public boolean useAdminCommand(String command, PlayerInstance activeChar)
	{
		if (command.equals("admin_invul"))
		{
			handleInvul(activeChar);
			AdminHtml.showAdminHtml(activeChar, "gm_menu.htm");
		}
		else if (command.equals("admin_undying"))
		{
			handleUndying(activeChar, activeChar);
			AdminHtml.showAdminHtml(activeChar, "gm_menu.htm");
		}
		
		else if (command.equals("admin_setinvul"))
		{
			final WorldObject target = activeChar.getTarget();
			if ((target != null) && target.isPlayer())
			{
				handleInvul((PlayerInstance) target);
			}
		}
		else if (command.equals("admin_setundying"))
		{
			final WorldObject target = activeChar.getTarget();
			if (target.isCreature())
			{
				handleUndying(activeChar, (Creature) target);
			}
		}
		return true;
	}
	
	@Override
	public String[] getAdminCommandList()
	{
		return ADMIN_COMMANDS;
	}
	
	private void handleInvul(PlayerInstance activeChar)
	{
		String text;
		if (activeChar.isInvul())
		{
			activeChar.setInvul(false);
			text = activeChar.getName() + " is now mortal.";
		}
		else
		{
			activeChar.setInvul(true);
			text = activeChar.getName() + " is now invulnerable.";
		}
		BuilderUtil.sendSysMessage(activeChar, text);
	}
	
	private void handleUndying(PlayerInstance activeChar, Creature target)
	{
		String text;
		if (target.isUndying())
		{
			target.setUndying(false);
			text = target.getName() + " is now mortal.";
		}
		else
		{
			target.setUndying(true);
			text = target.getName() + " is now undying.";
		}
		BuilderUtil.sendSysMessage(activeChar, text);
	}
}
