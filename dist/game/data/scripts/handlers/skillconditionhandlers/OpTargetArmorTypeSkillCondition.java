
package handlers.skillconditionhandlers;

import java.util.ArrayList;
import java.util.List;

import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.WorldObject;
import org.l2jdd.gameserver.model.actor.Creature;
import org.l2jdd.gameserver.model.itemcontainer.Inventory;
import org.l2jdd.gameserver.model.items.Item;
import org.l2jdd.gameserver.model.items.instance.ItemInstance;
import org.l2jdd.gameserver.model.items.type.ArmorType;
import org.l2jdd.gameserver.model.items.type.ItemType;
import org.l2jdd.gameserver.model.skills.ISkillCondition;
import org.l2jdd.gameserver.model.skills.Skill;

/**
 * @author Mobius
 */
public class OpTargetArmorTypeSkillCondition implements ISkillCondition
{
	private final List<ArmorType> _armorTypes = new ArrayList<>();
	
	public OpTargetArmorTypeSkillCondition(StatSet params)
	{
		final List<String> armorTypes = params.getList("armorType", String.class);
		if (armorTypes != null)
		{
			armorTypes.stream().map(ArmorType::valueOf).forEach(_armorTypes::add);
		}
	}
	
	@Override
	public boolean canUse(Creature caster, Skill skill, WorldObject target)
	{
		if ((target == null) || !target.isCreature())
		{
			return false;
		}
		
		final Creature targetCreature = (Creature) target;
		final Inventory inv = targetCreature.getInventory();
		
		// Get the chest armor.
		final ItemInstance chest = inv.getPaperdollItem(Inventory.PAPERDOLL_CHEST);
		if (chest == null)
		{
			return false;
		}
		
		// Get the chest item type.
		final ItemType chestType = chest.getItem().getItemType();
		
		// Get the chest body part.
		final long chestBodyPart = chest.getItem().getBodyPart();
		
		// Get the legs armor.
		final ItemInstance legs = inv.getPaperdollItem(Inventory.PAPERDOLL_LEGS);
		
		// Get the legs item type.
		ItemType legsType = null;
		if (legs != null)
		{
			legsType = legs.getItem().getItemType();
		}
		
		for (ArmorType armorType : _armorTypes)
		{
			// If chest armor is different from the condition one continue.
			if (chestType != armorType)
			{
				continue;
			}
			
			// Return true if chest armor is a full armor.
			if (chestBodyPart == Item.SLOT_FULL_ARMOR)
			{
				return true;
			}
			
			// Check legs armor.
			if (legs == null)
			{
				continue;
			}
			
			// Return true if legs armor matches too.
			if (legsType == armorType)
			{
				return true;
			}
		}
		
		return false;
	}
}
