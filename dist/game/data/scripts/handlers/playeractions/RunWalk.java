
package handlers.playeractions;

import org.l2jdd.gameserver.handler.IPlayerActionHandler;
import org.l2jdd.gameserver.model.ActionDataHolder;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;

/**
 * Run/Walk player action handler.
 * @author Mobius
 */
public class RunWalk implements IPlayerActionHandler
{
	@Override
	public void useAction(PlayerInstance player, ActionDataHolder data, boolean ctrlPressed, boolean shiftPressed)
	{
		if (player.isRunning())
		{
			player.setWalking();
		}
		else
		{
			player.setRunning();
		}
	}
}
