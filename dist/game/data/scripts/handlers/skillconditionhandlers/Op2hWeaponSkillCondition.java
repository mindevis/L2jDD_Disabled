
package handlers.skillconditionhandlers;

import java.util.ArrayList;
import java.util.List;

import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.WorldObject;
import org.l2jdd.gameserver.model.actor.Creature;
import org.l2jdd.gameserver.model.items.Item;
import org.l2jdd.gameserver.model.items.Weapon;
import org.l2jdd.gameserver.model.items.type.WeaponType;
import org.l2jdd.gameserver.model.skills.ISkillCondition;
import org.l2jdd.gameserver.model.skills.Skill;

/**
 * @author UnAfraid
 */
public class Op2hWeaponSkillCondition implements ISkillCondition
{
	private final List<WeaponType> _weaponTypes = new ArrayList<>();
	
	public Op2hWeaponSkillCondition(StatSet params)
	{
		final List<String> weaponTypes = params.getList("weaponType", String.class);
		if (weaponTypes != null)
		{
			weaponTypes.stream().map(WeaponType::valueOf).forEach(_weaponTypes::add);
		}
	}
	
	@Override
	public boolean canUse(Creature caster, Skill skill, WorldObject target)
	{
		final Weapon weapon = caster.getActiveWeaponItem();
		if (weapon == null)
		{
			return false;
		}
		return _weaponTypes.stream().anyMatch(weaponType -> (weapon.getItemType() == weaponType) && ((weapon.getBodyPart() & Item.SLOT_LR_HAND) != 0));
	}
}
