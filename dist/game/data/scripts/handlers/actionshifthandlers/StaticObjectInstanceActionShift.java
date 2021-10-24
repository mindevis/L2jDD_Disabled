
package handlers.actionshifthandlers;

import org.l2jdd.gameserver.enums.InstanceType;
import org.l2jdd.gameserver.handler.IActionShiftHandler;
import org.l2jdd.gameserver.model.WorldObject;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.model.actor.instance.StaticObjectInstance;
import org.l2jdd.gameserver.network.serverpackets.NpcHtmlMessage;
import org.l2jdd.gameserver.network.serverpackets.StaticObject;

public class StaticObjectInstanceActionShift implements IActionShiftHandler
{
	@Override
	public boolean action(PlayerInstance player, WorldObject target, boolean interact)
	{
		if (player.isGM())
		{
			player.setTarget(target);
			player.sendPacket(new StaticObject((StaticObjectInstance) target));
			
			final NpcHtmlMessage html = new NpcHtmlMessage(0, 1, "<html><body><center><font color=\"LEVEL\">Static Object Info</font></center><br><table border=0><tr><td>Coords X,Y,Z: </td><td>" + target.getX() + ", " + target.getY() + ", " + target.getZ() + "</td></tr><tr><td>Object ID: </td><td>" + target.getObjectId() + "</td></tr><tr><td>Static Object ID: </td><td>" + target.getId() + "</td></tr><tr><td>Mesh Index: </td><td>" + ((StaticObjectInstance) target).getMeshIndex() + "</td></tr><tr><td><br></td></tr><tr><td>Class: </td><td>" + target.getClass().getSimpleName() + "</td></tr></table></body></html>");
			player.sendPacket(html);
		}
		return true;
	}
	
	@Override
	public InstanceType getInstanceType()
	{
		return InstanceType.StaticObjectInstance;
	}
}
