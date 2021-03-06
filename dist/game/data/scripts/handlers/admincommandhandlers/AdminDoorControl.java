
package handlers.admincommandhandlers;

import java.awt.Color;
import java.util.logging.Logger;

import org.l2jdd.gameserver.data.xml.DoorData;
import org.l2jdd.gameserver.handler.IAdminCommandHandler;
import org.l2jdd.gameserver.instancemanager.CastleManager;
import org.l2jdd.gameserver.model.World;
import org.l2jdd.gameserver.model.WorldObject;
import org.l2jdd.gameserver.model.actor.instance.DoorInstance;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.model.siege.Castle;
import org.l2jdd.gameserver.network.serverpackets.ExServerPrimitive;
import org.l2jdd.gameserver.util.BuilderUtil;

/**
 * This class handles following admin commands: - open1 = open coloseum door 24190001 - open2 = open coloseum door 24190002 - open3 = open coloseum door 24190003 - open4 = open coloseum door 24190004 - openall = open all coloseum door - close1 = close coloseum door 24190001 - close2 = close coloseum
 * door 24190002 - close3 = close coloseum door 24190003 - close4 = close coloseum door 24190004 - closeall = close all coloseum door - open = open selected door - close = close selected door
 * @version $Revision: 1.2.4.5 $ $Date: 2005/04/11 10:06:06 $
 */
public class AdminDoorControl implements IAdminCommandHandler
{
	private static final Logger LOGGER = Logger.getLogger(AdminDoorControl.class.getName());
	
	private static DoorData _doorTable = DoorData.getInstance();
	
	private static final String[] ADMIN_COMMANDS =
	{
		"admin_open",
		"admin_close",
		"admin_openall",
		"admin_closeall",
		"admin_showdoors"
	};
	
	@Override
	public boolean useAdminCommand(String command, PlayerInstance activeChar)
	{
		try
		{
			if (command.startsWith("admin_open "))
			{
				final int doorId = Integer.parseInt(command.substring(11));
				if (_doorTable.getDoor(doorId) != null)
				{
					_doorTable.getDoor(doorId).openMe();
				}
				else
				{
					for (Castle castle : CastleManager.getInstance().getCastles())
					{
						if (castle.getDoor(doorId) != null)
						{
							castle.getDoor(doorId).openMe();
						}
					}
				}
			}
			else if (command.startsWith("admin_close "))
			{
				final int doorId = Integer.parseInt(command.substring(12));
				if (_doorTable.getDoor(doorId) != null)
				{
					_doorTable.getDoor(doorId).closeMe();
				}
				else
				{
					for (Castle castle : CastleManager.getInstance().getCastles())
					{
						if (castle.getDoor(doorId) != null)
						{
							castle.getDoor(doorId).closeMe();
						}
					}
				}
			}
			else if (command.equals("admin_closeall"))
			{
				for (DoorInstance door : _doorTable.getDoors())
				{
					door.closeMe();
				}
				for (Castle castle : CastleManager.getInstance().getCastles())
				{
					for (DoorInstance door : castle.getDoors())
					{
						door.closeMe();
					}
				}
			}
			else if (command.equals("admin_openall"))
			{
				for (DoorInstance door : _doorTable.getDoors())
				{
					door.openMe();
				}
				for (Castle castle : CastleManager.getInstance().getCastles())
				{
					for (DoorInstance door : castle.getDoors())
					{
						door.openMe();
					}
				}
			}
			else if (command.equals("admin_open"))
			{
				final WorldObject target = activeChar.getTarget();
				if ((target != null) && target.isDoor())
				{
					((DoorInstance) target).openMe();
				}
				else
				{
					BuilderUtil.sendSysMessage(activeChar, "Incorrect target.");
				}
			}
			else if (command.equals("admin_close"))
			{
				final WorldObject target = activeChar.getTarget();
				if ((target != null) && target.isDoor())
				{
					((DoorInstance) target).closeMe();
				}
				else
				{
					BuilderUtil.sendSysMessage(activeChar, "Incorrect target.");
				}
			}
			else if (command.equals("admin_showdoors"))
			{
				World.getInstance().forEachVisibleObject(activeChar, DoorInstance.class, door ->
				{
					final ExServerPrimitive packet = new ExServerPrimitive("door" + door.getId(), activeChar.getX(), activeChar.getY(), -16000);
					final Color color = door.isOpen() ? Color.GREEN : Color.RED;
					// box 1
					packet.addLine(color, door.getX(0), door.getY(0), door.getZMin(), door.getX(1), door.getY(1), door.getZMin());
					packet.addLine(color, door.getX(1), door.getY(1), door.getZMin(), door.getX(2), door.getY(2), door.getZMax());
					packet.addLine(color, door.getX(2), door.getY(2), door.getZMax(), door.getX(3), door.getY(3), door.getZMax());
					packet.addLine(color, door.getX(3), door.getY(3), door.getZMax(), door.getX(0), door.getY(0), door.getZMin());
					// box 2
					packet.addLine(color, door.getX(0), door.getY(0), door.getZMax(), door.getX(1), door.getY(1), door.getZMax());
					packet.addLine(color, door.getX(1), door.getY(1), door.getZMax(), door.getX(2), door.getY(2), door.getZMin());
					packet.addLine(color, door.getX(2), door.getY(2), door.getZMin(), door.getX(3), door.getY(3), door.getZMin());
					packet.addLine(color, door.getX(3), door.getY(3), door.getZMin(), door.getX(0), door.getY(0), door.getZMax());
					// diagonals
					packet.addLine(color, door.getX(0), door.getY(0), door.getZMin(), door.getX(1), door.getY(1), door.getZMax());
					packet.addLine(color, door.getX(2), door.getY(2), door.getZMin(), door.getX(3), door.getY(3), door.getZMax());
					packet.addLine(color, door.getX(0), door.getY(0), door.getZMax(), door.getX(1), door.getY(1), door.getZMin());
					packet.addLine(color, door.getX(2), door.getY(2), door.getZMax(), door.getX(3), door.getY(3), door.getZMin());
					activeChar.sendPacket(packet);
					// send message
					BuilderUtil.sendSysMessage(activeChar, "Found door " + door.getId());
				});
			}
		}
		catch (Exception e)
		{
			LOGGER.warning("Problem with AdminDoorControl: " + e.getMessage());
		}
		return true;
	}
	
	@Override
	public String[] getAdminCommandList()
	{
		return ADMIN_COMMANDS;
	}
}
