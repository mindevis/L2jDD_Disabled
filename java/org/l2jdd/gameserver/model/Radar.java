
package org.l2jdd.gameserver.model;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.network.serverpackets.RadarControl;

/**
 * @author dalrond
 */
public class Radar
{
	private final PlayerInstance _player;
	private final Set<RadarMarker> _markers = ConcurrentHashMap.newKeySet();
	
	public Radar(PlayerInstance player)
	{
		_player = player;
	}
	
	// Add a marker to player's radar
	public void addMarker(int x, int y, int z)
	{
		final RadarMarker newMarker = new RadarMarker(x, y, z);
		_markers.add(newMarker);
		_player.sendPacket(new RadarControl(2, 2, x, y, z));
		_player.sendPacket(new RadarControl(0, 1, x, y, z));
	}
	
	// Remove a marker from player's radar
	public void removeMarker(int x, int y, int z)
	{
		for (RadarMarker rm : _markers)
		{
			if ((rm._x == x) && (rm._y == y) && (rm._z == z))
			{
				_markers.remove(rm);
			}
		}
		_player.sendPacket(new RadarControl(1, 1, x, y, z));
	}
	
	public void removeAllMarkers()
	{
		for (RadarMarker tempMarker : _markers)
		{
			_player.sendPacket(new RadarControl(2, 2, tempMarker._x, tempMarker._y, tempMarker._z));
		}
		
		_markers.clear();
	}
	
	public void loadMarkers()
	{
		_player.sendPacket(new RadarControl(2, 2, _player.getX(), _player.getY(), _player.getZ()));
		for (RadarMarker tempMarker : _markers)
		{
			_player.sendPacket(new RadarControl(0, 1, tempMarker._x, tempMarker._y, tempMarker._z));
		}
	}
	
	public static class RadarMarker
	{
		// Simple class to model radar points.
		public int _type;
		public int _x;
		public int _y;
		public int _z;
		
		public RadarMarker(int type, int x, int y, int z)
		{
			_type = type;
			_x = x;
			_y = y;
			_z = z;
		}
		
		public RadarMarker(int x, int y, int z)
		{
			_type = 1;
			_x = x;
			_y = y;
			_z = z;
		}
		
		@Override
		public int hashCode()
		{
			final int prime = 31;
			int result = 1;
			result = (prime * result) + _type;
			result = (prime * result) + _x;
			result = (prime * result) + _y;
			result = (prime * result) + _z;
			return result;
		}
		
		@Override
		public boolean equals(Object obj)
		{
			if (this == obj)
			{
				return true;
			}
			if (!(obj instanceof RadarMarker))
			{
				return false;
			}
			final RadarMarker other = (RadarMarker) obj;
			return (_type == other._type) && (_x == other._x) && (_y == other._y) && (_z == other._z);
		}
	}
}
