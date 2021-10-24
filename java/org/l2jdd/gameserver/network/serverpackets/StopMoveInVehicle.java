
package org.l2jdd.gameserver.network.serverpackets;

import org.l2jdd.commons.network.PacketWriter;
import org.l2jdd.gameserver.model.Location;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.network.OutgoingPackets;

/**
 * @author Maktakien
 */
public class StopMoveInVehicle implements IClientOutgoingPacket
{
	private final int _objectId;
	private final int _boatId;
	private final Location _pos;
	private final int _heading;
	
	public StopMoveInVehicle(PlayerInstance player, int boatId)
	{
		_objectId = player.getObjectId();
		_boatId = boatId;
		_pos = player.getInVehiclePosition();
		_heading = player.getHeading();
	}
	
	@Override
	public boolean write(PacketWriter packet)
	{
		OutgoingPackets.STOP_MOVE_IN_VEHICLE.writeId(packet);
		
		packet.writeD(_objectId);
		packet.writeD(_boatId);
		packet.writeD(_pos.getX());
		packet.writeD(_pos.getY());
		packet.writeD(_pos.getZ());
		packet.writeD(_heading);
		return true;
	}
}
