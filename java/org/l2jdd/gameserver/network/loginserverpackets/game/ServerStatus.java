
package org.l2jdd.gameserver.network.loginserverpackets.game;

import java.util.ArrayList;
import java.util.List;

import org.l2jdd.commons.network.BaseSendablePacket;

/**
 * @author -Wooden-
 */
public class ServerStatus extends BaseSendablePacket
{
	private final List<Attribute> _attributes;
	
	public static final String[] STATUS_STRING =
	{
		"Auto",
		"Good",
		"Normal",
		"Full",
		"Down",
		"Gm Only"
	};
	
	public static final int SERVER_LIST_STATUS = 0x01;
	public static final int SERVER_TYPE = 0x02;
	public static final int SERVER_LIST_SQUARE_BRACKET = 0x03;
	public static final int MAX_PLAYERS = 0x04;
	public static final int SERVER_AGE = 0x05;
	
	// Server Status
	public static final int STATUS_AUTO = 0x00;
	public static final int STATUS_GOOD = 0x01;
	public static final int STATUS_NORMAL = 0x02;
	public static final int STATUS_FULL = 0x03;
	public static final int STATUS_DOWN = 0x04;
	public static final int STATUS_GM_ONLY = 0x05;
	
	// Server Types
	public static final int SERVER_NORMAL = 0x01;
	public static final int SERVER_RELAX = 0x02;
	public static final int SERVER_TEST = 0x04;
	public static final int SERVER_NOLABEL = 0x08;
	public static final int SERVER_CREATION_RESTRICTED = 0x10;
	public static final int SERVER_EVENT = 0x20;
	public static final int SERVER_FREE = 0x40;
	
	// Server Ages
	public static final int SERVER_AGE_ALL = 0x00;
	public static final int SERVER_AGE_15 = 0x0F;
	public static final int SERVER_AGE_18 = 0x12;
	
	public static final int ON = 0x01;
	public static final int OFF = 0x00;
	
	static class Attribute
	{
		public int id;
		public int value;
		
		Attribute(int pId, int pValue)
		{
			id = pId;
			value = pValue;
		}
	}
	
	public ServerStatus()
	{
		_attributes = new ArrayList<>();
	}
	
	public void addAttribute(int id, int value)
	{
		_attributes.add(new Attribute(id, value));
	}
	
	@Override
	public byte[] getContent()
	{
		writeC(0x06);
		writeD(_attributes.size());
		for (Attribute temp : _attributes)
		{
			writeD(temp.id);
			writeD(temp.value);
		}
		return getBytes();
	}
}