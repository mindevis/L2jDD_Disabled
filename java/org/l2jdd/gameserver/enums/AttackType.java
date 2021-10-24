
package org.l2jdd.gameserver.enums;

/**
 * @author UnAfraid
 */
public enum AttackType
{
	MISSED(0x01),
	BLOCKED(0x02),
	CRITICAL(0x04),
	SHOT_USED(0x08);
	
	private final int _mask;
	
	private AttackType(int mask)
	{
		_mask = mask;
	}
	
	public int getMask()
	{
		return _mask;
	}
}
