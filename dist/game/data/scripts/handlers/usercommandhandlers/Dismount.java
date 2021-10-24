
package handlers.usercommandhandlers;

import org.l2jdd.gameserver.handler.IUserCommandHandler;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;

/**
 * Dismount user command.
 * @author Micht
 */
public class Dismount implements IUserCommandHandler
{
	private static final int[] COMMAND_IDS =
	{
		62
	};
	
	@Override
	public synchronized boolean useUserCommand(int id, PlayerInstance player)
	{
		if (id != COMMAND_IDS[0])
		{
			return false;
		}
		
		if (player.isRentedPet())
		{
			player.stopRentPet();
		}
		else if (player.isMounted())
		{
			player.dismount();
		}
		return true;
	}
	
	@Override
	public int[] getUserCommandList()
	{
		return COMMAND_IDS;
	}
}
