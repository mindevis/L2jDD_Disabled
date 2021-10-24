
package handlers.admincommandhandlers;

import org.l2jdd.gameserver.handler.IAdminCommandHandler;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.network.SystemMessageId;
import org.l2jdd.gameserver.util.BuilderUtil;

/**
 * @author
 */
public class AdminRide implements IAdminCommandHandler
{
	private static final String[] ADMIN_COMMANDS =
	{
		"admin_ride_horse",
		"admin_ride_bike",
		"admin_ride_wyvern",
		"admin_ride_strider",
		"admin_unride_wyvern",
		"admin_unride_strider",
		"admin_unride",
		"admin_ride_wolf",
		"admin_unride_wolf",
	};
	
	private static final int PURPLE_MANED_HORSE_TRANSFORMATION_ID = 106;
	
	private static final int JET_BIKE_TRANSFORMATION_ID = 20001;
	
	@Override
	public boolean useAdminCommand(String command, PlayerInstance activeChar)
	{
		final PlayerInstance player = getRideTarget(activeChar);
		if (player == null)
		{
			return false;
		}
		
		if (command.startsWith("admin_ride"))
		{
			if (player.isMounted() || player.hasSummon())
			{
				BuilderUtil.sendSysMessage(activeChar, "Target already have a summon.");
				return false;
			}
			
			int petRideId;
			if (command.startsWith("admin_ride_wyvern"))
			{
				petRideId = 12621;
			}
			else if (command.startsWith("admin_ride_strider"))
			{
				petRideId = 12526;
			}
			else if (command.startsWith("admin_ride_wolf"))
			{
				petRideId = 16041;
			}
			else if (command.startsWith("admin_ride_horse")) // handled using transformation
			{
				if (player.isTransformed())
				{
					activeChar.sendPacket(SystemMessageId.YOU_ALREADY_POLYMORPHED_AND_CANNOT_POLYMORPH_AGAIN);
				}
				else
				{
					player.transform(PURPLE_MANED_HORSE_TRANSFORMATION_ID, true);
				}
				
				return true;
			}
			else if (command.startsWith("admin_ride_bike")) // handled using transformation
			{
				if (player.isTransformed())
				{
					activeChar.sendPacket(SystemMessageId.YOU_ALREADY_POLYMORPHED_AND_CANNOT_POLYMORPH_AGAIN);
				}
				else
				{
					player.transform(JET_BIKE_TRANSFORMATION_ID, true);
				}
				
				return true;
			}
			else
			{
				BuilderUtil.sendSysMessage(activeChar, "Command '" + command + "' not recognized");
				return false;
			}
			
			player.mount(petRideId, 0, false);
			return false;
		}
		else if (command.startsWith("admin_unride"))
		{
			if (player.getTransformationId() == PURPLE_MANED_HORSE_TRANSFORMATION_ID)
			{
				player.untransform();
			}
			
			if (player.getTransformationId() == JET_BIKE_TRANSFORMATION_ID)
			{
				player.untransform();
			}
			else
			{
				player.dismount();
			}
		}
		return true;
	}
	
	private PlayerInstance getRideTarget(PlayerInstance activeChar)
	{
		PlayerInstance player = null;
		if ((activeChar.getTarget() == null) || (activeChar.getTarget().getObjectId() == activeChar.getObjectId()) || !activeChar.getTarget().isPlayer())
		{
			player = activeChar;
		}
		else
		{
			player = (PlayerInstance) activeChar.getTarget();
		}
		return player;
	}
	
	@Override
	public String[] getAdminCommandList()
	{
		return ADMIN_COMMANDS;
	}
}
