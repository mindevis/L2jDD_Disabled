
package handlers.usercommandhandlers;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.l2jdd.Config;
import org.l2jdd.commons.util.Chronos;
import org.l2jdd.gameserver.GameTimeController;
import org.l2jdd.gameserver.handler.IUserCommandHandler;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.network.SystemMessageId;
import org.l2jdd.gameserver.network.serverpackets.SystemMessage;

/**
 * Time user command.
 */
public class Time implements IUserCommandHandler
{
	private static final int[] COMMAND_IDS =
	{
		77
	};
	
	private static final SimpleDateFormat SDF = new SimpleDateFormat("H:mm.");
	
	@Override
	public boolean useUserCommand(int id, PlayerInstance player)
	{
		if (COMMAND_IDS[0] != id)
		{
			return false;
		}
		
		final int t = GameTimeController.getInstance().getGameTime();
		final String h = Integer.toString(((t / 60) % 24));
		String m;
		if ((t % 60) < 10)
		{
			m = "0" + (t % 60);
		}
		else
		{
			m = Integer.toString((t % 60));
		}
		
		SystemMessage sm;
		if (GameTimeController.getInstance().isNight())
		{
			sm = new SystemMessage(SystemMessageId.THE_CURRENT_TIME_IS_S1_S2_2);
			sm.addString(h);
			sm.addString(m);
		}
		else
		{
			sm = new SystemMessage(SystemMessageId.THE_CURRENT_TIME_IS_S1_S2);
			sm.addString(h);
			sm.addString(m);
		}
		player.sendPacket(sm);
		if (Config.DISPLAY_SERVER_TIME)
		{
			player.sendMessage("Server time is " + SDF.format(new Date(Chronos.currentTimeMillis())));
		}
		return true;
	}
	
	@Override
	public int[] getUserCommandList()
	{
		return COMMAND_IDS;
	}
}
