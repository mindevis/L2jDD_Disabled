
package org.l2jdd.gameserver.enums;

import org.l2jdd.gameserver.data.xml.CategoryData;

/**
 * @author UnAfraid
 */
public enum MountType
{
	NONE,
	STRIDER,
	WYVERN,
	WOLF;
	
	public static MountType findByNpcId(int npcId)
	{
		if (CategoryData.getInstance().isInCategory(CategoryType.STRIDER, npcId))
		{
			return STRIDER;
		}
		else if (CategoryData.getInstance().isInCategory(CategoryType.WYVERN_GROUP, npcId))
		{
			return WYVERN;
		}
		else if (CategoryData.getInstance().isInCategory(CategoryType.WOLF_GROUP, npcId))
		{
			return WOLF;
		}
		return NONE;
	}
}