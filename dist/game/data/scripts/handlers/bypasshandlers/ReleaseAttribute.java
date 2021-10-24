
package handlers.bypasshandlers;

import org.l2jdd.gameserver.handler.IBypassHandler;
import org.l2jdd.gameserver.model.actor.Creature;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.network.serverpackets.ExShowBaseAttributeCancelWindow;

public class ReleaseAttribute implements IBypassHandler
{
	private static final String[] COMMANDS =
	{
		"ReleaseAttribute"
	};
	
	@Override
	public boolean useBypass(String command, PlayerInstance player, Creature target)
	{
		if (!target.isNpc())
		{
			return false;
		}
		
		player.sendPacket(new ExShowBaseAttributeCancelWindow(player));
		return true;
	}
	
	@Override
	public String[] getBypassList()
	{
		return COMMANDS;
	}
}
