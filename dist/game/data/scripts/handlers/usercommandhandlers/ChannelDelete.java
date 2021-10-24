
package handlers.usercommandhandlers;

import org.l2jdd.gameserver.handler.IUserCommandHandler;
import org.l2jdd.gameserver.model.CommandChannel;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.network.SystemMessageId;
import org.l2jdd.gameserver.network.serverpackets.SystemMessage;

/**
 * Channel Delete user command.
 * @author Chris
 */
public class ChannelDelete implements IUserCommandHandler
{
	private static final int[] COMMAND_IDS =
	{
		93
	};
	
	@Override
	public boolean useUserCommand(int id, PlayerInstance player)
	{
		if (id != COMMAND_IDS[0])
		{
			return false;
		}
		
		if (player.isInParty() && player.getParty().isLeader(player) && player.getParty().isInCommandChannel() && player.getParty().getCommandChannel().getLeader().equals(player))
		{
			final CommandChannel channel = player.getParty().getCommandChannel();
			channel.broadcastPacket(new SystemMessage(SystemMessageId.THE_COMMAND_CHANNEL_HAS_BEEN_DISBANDED));
			channel.disbandChannel();
			return true;
		}
		
		return false;
	}
	
	@Override
	public int[] getUserCommandList()
	{
		return COMMAND_IDS;
	}
}
