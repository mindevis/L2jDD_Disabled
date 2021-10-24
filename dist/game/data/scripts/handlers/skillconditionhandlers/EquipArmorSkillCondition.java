
package handlers.skillconditionhandlers;

import java.util.List;

import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.WorldObject;
import org.l2jdd.gameserver.model.actor.Creature;
import org.l2jdd.gameserver.model.itemcontainer.Inventory;
import org.l2jdd.gameserver.model.items.Item;
import org.l2jdd.gameserver.model.items.instance.ItemInstance;
import org.l2jdd.gameserver.model.items.type.ArmorType;
import org.l2jdd.gameserver.model.skills.ISkillCondition;
import org.l2jdd.gameserver.model.skills.Skill;

/**
 * @author Sdw
 */
public class EquipArmorSkillCondition implements ISkillCondition
{
	private int _armorTypesMask = 0;
	
	public EquipArmorSkillCondition(StatSet params)
	{
		final List<ArmorType> armorTypes = params.getEnumList("armorType", ArmorType.class);
		if (armorTypes != null)
		{
			for (ArmorType armorType : armorTypes)
			{
				_armorTypesMask |= armorType.mask();
			}
		}
	}
	
	@Override
	public boolean canUse(Creature caster, Skill skill, WorldObject target)
	{
		if ((caster == null) || !caster.isPlayer())
		{
			return false;
		}
		
		final Inventory inv = caster.getInventory();
		
		// Get the itemMask of the weared chest (if exists)
		final ItemInstance chest = inv.getPaperdollItem(Inventory.PAPERDOLL_CHEST);
		if (chest == null)
		{
			return false;
		}
		
		// If chest armor is different from the condition one return false
		final int chestMask = chest.getItem().getItemMask();
		if ((_armorTypesMask & chestMask) == 0)
		{
			return false;
		}
		
		// So from here, chest armor matches conditions
		
		// return True if chest armor is a Full Armor
		final long chestBodyPart = chest.getItem().getBodyPart();
		if (chestBodyPart == Item.SLOT_FULL_ARMOR)
		{
			return true;
		}
		
		// check legs armor
		final ItemInstance legs = inv.getPaperdollItem(Inventory.PAPERDOLL_LEGS);
		if (legs == null)
		{
			return false;
		}
		
		// return true if legs armor matches too
		final int legMask = legs.getItem().getItemMask();
		return (_armorTypesMask & legMask) != 0;
	}
}
