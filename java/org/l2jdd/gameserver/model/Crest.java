
package org.l2jdd.gameserver.model;

import org.l2jdd.Config;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.model.interfaces.IIdentifiable;
import org.l2jdd.gameserver.network.serverpackets.AllyCrest;
import org.l2jdd.gameserver.network.serverpackets.ExPledgeEmblem;
import org.l2jdd.gameserver.network.serverpackets.PledgeCrest;

/**
 * @author NosBit
 */
public class Crest implements IIdentifiable
{
	public enum CrestType
	{
		PLEDGE(1),
		PLEDGE_LARGE(2),
		ALLY(3);
		
		private final int _id;
		
		CrestType(int id)
		{
			_id = id;
		}
		
		public int getId()
		{
			return _id;
		}
		
		public static CrestType getById(int id)
		{
			for (CrestType crestType : values())
			{
				if (crestType.getId() == id)
				{
					return crestType;
				}
			}
			return null;
		}
	}
	
	private final int _id;
	private final byte[] _data;
	private final CrestType _type;
	
	public Crest(int id, byte[] data, CrestType type)
	{
		_id = id;
		_data = data;
		_type = type;
	}
	
	@Override
	public int getId()
	{
		return _id;
	}
	
	public byte[] getData()
	{
		return _data;
	}
	
	public CrestType getType()
	{
		return _type;
	}
	
	/**
	 * Gets the client path to crest for use in html and sends the crest to {@code PlayerInstance}
	 * @param player the @{code PlayerInstance} where html is send to.
	 * @return the client path to crest
	 */
	public String getClientPath(PlayerInstance player)
	{
		String path = null;
		switch (_type)
		{
			case PLEDGE:
			{
				player.sendPacket(new PledgeCrest(_id, _data));
				path = "Crest.crest_" + Config.SERVER_ID + "_" + _id;
				break;
			}
			case PLEDGE_LARGE:
			{
				if (_data != null)
				{
					for (int i = 0; i <= 4; i++)
					{
						if (i < 4)
						{
							final byte[] fullChunk = new byte[14336];
							System.arraycopy(_data, (14336 * i), fullChunk, 0, 14336);
							player.sendPacket(new ExPledgeEmblem(_id, fullChunk, 0, i));
						}
						else
						{
							final byte[] lastChunk = new byte[8320];
							System.arraycopy(_data, (14336 * i), lastChunk, 0, 8320);
							player.sendPacket(new ExPledgeEmblem(_id, lastChunk, 0, i));
						}
					}
				}
				path = "Crest.crest_" + Config.SERVER_ID + "_" + _id + "_l";
				break;
			}
			case ALLY:
			{
				player.sendPacket(new AllyCrest(_id, _data));
				path = "Crest.crest_" + Config.SERVER_ID + "_" + _id;
				break;
			}
		}
		return path;
	}
}