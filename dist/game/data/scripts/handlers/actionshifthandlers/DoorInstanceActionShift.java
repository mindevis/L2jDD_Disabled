
package handlers.actionshifthandlers;

import org.l2jdd.gameserver.data.xml.ClanHallData;
import org.l2jdd.gameserver.enums.InstanceType;
import org.l2jdd.gameserver.handler.IActionShiftHandler;
import org.l2jdd.gameserver.model.WorldObject;
import org.l2jdd.gameserver.model.actor.instance.DoorInstance;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.model.residences.ClanHall;
import org.l2jdd.gameserver.model.siege.Castle;
import org.l2jdd.gameserver.model.siege.Fort;
import org.l2jdd.gameserver.network.serverpackets.NpcHtmlMessage;
import org.l2jdd.gameserver.network.serverpackets.StaticObject;
import org.l2jdd.gameserver.util.HtmlUtil;

/**
 * This class manage shift + click on {@link DoorInstance}.
 * @author St3eT
 */
public class DoorInstanceActionShift implements IActionShiftHandler
{
	@Override
	public boolean action(PlayerInstance player, WorldObject target, boolean interact)
	{
		if (player.isGM())
		{
			player.setTarget(target);
			final DoorInstance door = (DoorInstance) target;
			final ClanHall clanHall = ClanHallData.getInstance().getClanHallByDoorId(door.getId());
			final Fort fort = door.getFort();
			final Castle castle = door.getCastle();
			player.sendPacket(new StaticObject(door, player.isGM()));
			final NpcHtmlMessage html = new NpcHtmlMessage(0, 1);
			html.setFile(player, "data/html/admin/doorinfo.htm");
			// Hp / MP
			html.replace("%hpGauge%", HtmlUtil.getHpGauge(250, (long) door.getCurrentHp(), door.getMaxHp(), false));
			html.replace("%mpGauge%", HtmlUtil.getMpGauge(250, (long) door.getCurrentMp(), door.getMaxMp(), false));
			// Basic info
			html.replace("%doorName%", door.getName());
			html.replace("%objId%", String.valueOf(door.getObjectId()));
			html.replace("%doorId%", String.valueOf(door.getId()));
			// Position info
			html.replace("%position%", door.getX() + ", " + door.getY() + ", " + door.getZ());
			html.replace("%node1%", door.getX(0) + ", " + door.getY(0) + ", " + door.getZMin());
			html.replace("%node2%", door.getX(1) + ", " + door.getY(1) + ", " + door.getZMin());
			html.replace("%node3%", door.getX(2) + ", " + door.getY(2) + ", " + door.getZMax());
			html.replace("%node4%", door.getX(3) + ", " + door.getY(3) + ", " + door.getZMax());
			// Residence info
			html.replace("%fortress%", fort != null ? fort.getName() : "None");
			html.replace("%clanHall%", clanHall != null ? clanHall.getName() : "None");
			html.replace("%castle%", castle != null ? castle.getName() + " Castle" : "None");
			player.sendPacket(html);
		}
		return true;
	}
	
	@Override
	public InstanceType getInstanceType()
	{
		return InstanceType.DoorInstance;
	}
}