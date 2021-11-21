/*
 * This file is part of the L2J Mobius project.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.l2jdd.gameserver.network.serverpackets;

import org.l2jdd.commons.network.PacketWriter;
import org.l2jdd.gameserver.model.actor.instance.BoatInstance;
import org.l2jdd.gameserver.network.OutgoingPackets;

/**
 * @author Maktakien
 */
public class OnVehicleCheckLocation implements IClientOutgoingPacket
{
	private final BoatInstance _boat;
	private final int _x;
	private final int _y;
	private final int _z;
	
	/**
	 * @param instance
	 * @param x
	 * @param y
	 * @param z
	 */
	public OnVehicleCheckLocation(BoatInstance instance, int x, int y, int z)
	{
		_boat = instance;
		_x = x;
		_y = y;
		_z = z;
	}
	
	@Override
	public boolean write(PacketWriter packet)
	{
		OutgoingPackets.ON_VEHICLE_CHECK_LOCATION.writeId(packet);
		packet.writeD(_boat.getObjectId());
		packet.writeD(_x);
		packet.writeD(_y);
		packet.writeD(_z);
		packet.writeD(_boat.getPosition().getHeading());
		return true;
	}
}