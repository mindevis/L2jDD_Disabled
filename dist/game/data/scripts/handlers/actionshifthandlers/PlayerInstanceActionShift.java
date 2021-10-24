
package handlers.actionshifthandlers;

import org.l2jdd.gameserver.enums.InstanceType;
import org.l2jdd.gameserver.handler.AdminCommandHandler;
import org.l2jdd.gameserver.handler.IActionShiftHandler;
import org.l2jdd.gameserver.model.WorldObject;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;

public class PlayerInstanceActionShift implements IActionShiftHandler
{
	@Override
	public boolean action(PlayerInstance player, WorldObject target, boolean interact)
	{
		if (player.isGM())
		{
			// Check if the GM already target this l2pcinstance
			if (player.getTarget() != target)
			{
				// Set the target of the PlayerInstance player
				player.setTarget(target);
			}
			
			AdminCommandHandler.getInstance().useAdminCommand(player, "admin_character_info " + target.getName(), true);
		}
		return true;
	}
	
	@Override
	public InstanceType getInstanceType()
	{
		return InstanceType.PlayerInstance;
	}
}
