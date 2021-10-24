
package handlers.usercommandhandlers;

import org.l2jdd.gameserver.handler.IUserCommandHandler;
import org.l2jdd.gameserver.model.CommandChannel;
import org.l2jdd.gameserver.model.Party;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.network.SystemMessageId;
import org.l2jdd.gameserver.network.serverpackets.SystemMessage;

/**
 * Channel Leave user command.
 * @author Chris, Zoey76
 */
public class ChannelLeave implements IUserCommandHandler
{
	private static final int[] COMMAND_IDS =
	{
		96
	};
	
	@Override
	public boolean useUserCommand(int id, PlayerInstance player)
	{
		if (id != COMMAND_IDS[0])
		{
			return false;
		}
		
		if (!player.isInParty() || !player.getParty().isLeader(player))
		{
			player.sendPacket(SystemMessageId.ONLY_A_PARTY_LEADER_CAN_LEAVE_A_COMMAND_CHANNEL);
			return false;
		}
		
		if (player.getParty().isInCommandChannel())
		{
			final CommandChannel channel = player.getParty().getCommandChannel();
			final Party party = player.getParty();
			channel.removeParty(party);
			party.getLeader().sendPacket(SystemMessageId.YOU_HAVE_QUIT_THE_COMMAND_CHANNEL);
			
			final SystemMessage sm = new SystemMessage(SystemMessageId.C1_S_PARTY_HAS_LEFT_THE_COMMAND_CHANNEL);
			sm.addPcName(party.getLeader());
			channel.broadcastPacket(sm);
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
