
package org.l2jdd.gameserver.enums;

import org.l2jdd.gameserver.model.interfaces.IUpdateTypeComponent;

/**
 * @author UnAfraid
 */
public enum PartySmallWindowUpdateType implements IUpdateTypeComponent
{
	CURRENT_CP(0x01),
	MAX_CP(0x02),
	CURRENT_HP(0x04),
	MAX_HP(0x08),
	CURRENT_MP(0x10),
	MAX_MP(0x20),
	LEVEL(0x40),
	CLASS_ID(0x80),
	PARTY_SUBSTITUTE(0x100),
	VITALITY_POINTS(0x200);
	
	private final int _mask;
	
	private PartySmallWindowUpdateType(int mask)
	{
		_mask = mask;
	}
	
	@Override
	public int getMask()
	{
		return _mask;
	}
}
