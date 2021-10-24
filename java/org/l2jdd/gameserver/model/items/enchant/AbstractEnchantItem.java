
package org.l2jdd.gameserver.model.items.enchant;

import java.util.logging.Logger;

import org.l2jdd.commons.util.CommonUtil;
import org.l2jdd.gameserver.data.ItemTable;
import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.items.Item;
import org.l2jdd.gameserver.model.items.instance.ItemInstance;
import org.l2jdd.gameserver.model.items.type.CrystalType;
import org.l2jdd.gameserver.model.items.type.EtcItemType;
import org.l2jdd.gameserver.model.items.type.ItemType;

/**
 * @author UnAfraid
 */
public abstract class AbstractEnchantItem
{
	protected static final Logger LOGGER = Logger.getLogger(AbstractEnchantItem.class.getName());
	
	private static final ItemType[] ENCHANT_TYPES = new ItemType[]
	{
		EtcItemType.ENCHT_ATTR_ANCIENT_CRYSTAL_ENCHANT_AM,
		EtcItemType.ENCHT_ATTR_ANCIENT_CRYSTAL_ENCHANT_WP,
		EtcItemType.BLESS_ENCHT_AM,
		EtcItemType.BLESS_ENCHT_AM_DOWN,
		EtcItemType.BLESS_ENCHT_WP,
		EtcItemType.ENCHT_AM,
		EtcItemType.ENCHT_WP,
		EtcItemType.GIANT_ENCHT_AM,
		EtcItemType.GIANT_ENCHT_WP,
		EtcItemType.ENCHT_ATTR_INC_PROP_ENCHT_AM,
		EtcItemType.ENCHT_ATTR_INC_PROP_ENCHT_WP,
		EtcItemType.GIANT_ENCHT_ATTR_INC_PROP_ENCHT_AM,
		EtcItemType.GIANT_ENCHT_ATTR_INC_PROP_ENCHT_WP,
		EtcItemType.BLESSED_ENCHT_ATTR_INC_PROP_ENCHT_AM,
		EtcItemType.BLESSED_ENCHT_ATTR_INC_PROP_ENCHT_WP,
		EtcItemType.BLESSED_GIANT_ENCHT_ATTR_INC_PROP_ENCHT_AM,
		EtcItemType.BLESSED_GIANT_ENCHT_ATTR_INC_PROP_ENCHT_WP,
	};
	
	private final int _id;
	private final CrystalType _grade;
	private final int _minEnchantLevel;
	private final int _maxEnchantLevel;
	private final int _safeEnchantLevel;
	private final double _bonusRate;
	
	public AbstractEnchantItem(StatSet set)
	{
		_id = set.getInt("id");
		if (getItem() == null)
		{
			throw new NullPointerException();
		}
		else if (!CommonUtil.contains(ENCHANT_TYPES, getItem().getItemType()))
		{
			throw new IllegalAccessError();
		}
		_grade = set.getEnum("targetGrade", CrystalType.class, CrystalType.NONE);
		_minEnchantLevel = set.getInt("minEnchant", 0);
		_maxEnchantLevel = set.getInt("maxEnchant", 127);
		_safeEnchantLevel = set.getInt("safeEnchant", 0);
		_bonusRate = set.getDouble("bonusRate", 0);
	}
	
	/**
	 * @return id of current item
	 */
	public int getId()
	{
		return _id;
	}
	
	/**
	 * @return bonus chance that would be added
	 */
	public double getBonusRate()
	{
		return _bonusRate;
	}
	
	/**
	 * @return {@link Item} current item/scroll
	 */
	public Item getItem()
	{
		return ItemTable.getInstance().getTemplate(_id);
	}
	
	/**
	 * @return grade of the item/scroll.
	 */
	public CrystalType getGrade()
	{
		return _grade;
	}
	
	/**
	 * @return {@code true} if scroll is for weapon, {@code false} for armor
	 */
	public abstract boolean isWeapon();
	
	/**
	 * @return the minimum enchant level that this scroll/item can be used with
	 */
	public int getMinEnchantLevel()
	{
		return _minEnchantLevel;
	}
	
	/**
	 * @return the maximum enchant level that this scroll/item can be used with
	 */
	public int getMaxEnchantLevel()
	{
		return _maxEnchantLevel;
	}
	
	/**
	 * @return the safe enchant level of this scroll/item
	 */
	public int getSafeEnchant()
	{
		return _safeEnchantLevel;
	}
	
	/**
	 * @param itemToEnchant the item to be enchanted
	 * @param supportItem
	 * @return {@code true} if this support item can be used with the item to be enchanted, {@code false} otherwise
	 */
	public boolean isValid(ItemInstance itemToEnchant, EnchantSupportItem supportItem)
	{
		if (itemToEnchant == null)
		{
			return false;
		}
		else if (!itemToEnchant.isEnchantable() || (!(itemToEnchant.getItem().getEnchantLimit() == 0) && (itemToEnchant.getEnchantLevel() == itemToEnchant.getItem().getEnchantLimit())))
		{
			return false;
		}
		else if (!isValidItemType(itemToEnchant.getItem().getType2()))
		{
			return false;
		}
		else if (((_minEnchantLevel != 0) && (itemToEnchant.getEnchantLevel() < _minEnchantLevel)) || ((_maxEnchantLevel != 0) && (itemToEnchant.getEnchantLevel() >= _maxEnchantLevel)))
		{
			return false;
		}
		else if (_grade != itemToEnchant.getItem().getCrystalTypePlus())
		{
			return false;
		}
		return true;
	}
	
	/**
	 * @param type2
	 * @return {@code true} if current type2 is valid to be enchanted, {@code false} otherwise
	 */
	private boolean isValidItemType(int type2)
	{
		if (type2 == Item.TYPE2_WEAPON)
		{
			return isWeapon();
		}
		else if ((type2 == Item.TYPE2_SHIELD_ARMOR) || (type2 == Item.TYPE2_ACCESSORY))
		{
			return !isWeapon();
		}
		return false;
	}
}
