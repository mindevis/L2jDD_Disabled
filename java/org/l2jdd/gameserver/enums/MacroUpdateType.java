
package org.l2jdd.gameserver.enums;

/**
 * @author jeremy
 */
public enum MacroUpdateType
{
	ADD(0x01),
	LIST(0x01),
	MODIFY(0x02),
	DELETE(0x00);
	
	private final int _id;
	
	private MacroUpdateType(int id)
	{
		_id = id;
	}
	
	public int getId()
	{
		return _id;
	}
}
