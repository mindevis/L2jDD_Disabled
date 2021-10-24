
package org.l2jdd.gameserver.model.items.type;

/**
 * Crystal Type enumerated.
 * @author Adry_85
 */
public enum CrystalType
{
	NONE(0, 0, 0, 0),
	D(1, 1458, 11, 90),
	C(2, 1459, 6, 45),
	B(3, 1460, 11, 67),
	A(4, 1461, 20, 145),
	S(5, 1462, 25, 250),
	S80(6, 1462, 25, 250),
	S84(7, 1462, 25, 250),
	R(8, 17371, 30, 500),
	R95(9, 17371, 30, 500),
	R99(10, 17371, 30, 500),
	R110(11, 17371, 30, 500),
	EVENT(12, 0, 0, 0);
	
	private final int _level;
	private final int _crystalId;
	private final int _crystalEnchantBonusArmor;
	private final int _crystalEnchantBonusWeapon;
	
	CrystalType(int level, int crystalId, int crystalEnchantBonusArmor, int crystalEnchantBonusWeapon)
	{
		_level = level;
		_crystalId = crystalId;
		_crystalEnchantBonusArmor = crystalEnchantBonusArmor;
		_crystalEnchantBonusWeapon = crystalEnchantBonusWeapon;
	}
	
	/**
	 * Gets the crystal type ID.
	 * @return the crystal type ID
	 */
	public int getLevel()
	{
		return _level;
	}
	
	/**
	 * Gets the item ID of the crystal.
	 * @return the item ID of the crystal
	 */
	public int getCrystalId()
	{
		return _crystalId;
	}
	
	public int getCrystalEnchantBonusArmor()
	{
		return _crystalEnchantBonusArmor;
	}
	
	public int getCrystalEnchantBonusWeapon()
	{
		return _crystalEnchantBonusWeapon;
	}
	
	public boolean isGreater(CrystalType crystalType)
	{
		return getLevel() > crystalType.getLevel();
	}
	
	public boolean isLesser(CrystalType crystalType)
	{
		return getLevel() < crystalType.getLevel();
	}
	
	public CrystalType plusLevel(int levelValue)
	{
		final int level = levelValue + _level;
		if (level >= CrystalType.R110.getLevel())
		{
			return CrystalType.R110;
		}
		if (level <= CrystalType.NONE.getLevel())
		{
			return CrystalType.NONE;
		}
		return getByLevel(level);
	}
	
	public static CrystalType getByLevel(int level)
	{
		for (CrystalType crystalType : values())
		{
			if (crystalType.getLevel() == level)
			{
				return crystalType;
			}
		}
		return null;
	}
}
