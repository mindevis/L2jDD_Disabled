
package org.l2jdd.gameserver.network.clientpackets;

import org.l2jdd.commons.network.PacketReader;
import org.l2jdd.gameserver.model.Location;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.network.GameClient;
import org.l2jdd.gameserver.network.serverpackets.StopMoveInVehicle;

/**
 * @author Maktakien
 */
public class CannotMoveAnymoreInVehicle implements IClientIncomingPacket
{
	private int _x;
	private int _y;
	private int _z;
	private int _heading;
	private int _boatId;
	
	@Override
	public boolean read(GameClient client, PacketReader packet)
	{
		_boatId = packet.readD();
		_x = packet.readD();
		_y = packet.readD();
		_z = packet.readD();
		_heading = packet.readD();
		return true;
	}
	
	@Override
	public void run(GameClient client)
	{
		final PlayerInstance player = client.getPlayer();
		if (player == null)
		{
			return;
		}
		if (player.isInBoat() && (player.getBoat().getObjectId() == _boatId))
		{
			player.setInVehiclePosition(new Location(_x, _y, _z));
			player.setHeading(_heading);
			player.broadcastPacket(new StopMoveInVehicle(player, _boatId));
		}
	}
}
