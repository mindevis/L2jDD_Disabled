
package org.l2jdd.gameserver.enums;

/**
 * @author NosBit
 */
public enum Team
{
	NONE(0),
	BLUE(1),
	RED(2);
	
	private int _id;
	
	private Team(int id)
	{
		_id = id;
	}
	
	public int getId()
	{
		return _id;
	}
}
