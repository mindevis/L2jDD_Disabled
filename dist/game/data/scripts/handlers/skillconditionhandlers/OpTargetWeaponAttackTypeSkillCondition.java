
package handlers.skillconditionhandlers;

import java.util.ArrayList;
import java.util.List;

import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.WorldObject;
import org.l2jdd.gameserver.model.actor.Creature;
import org.l2jdd.gameserver.model.items.Weapon;
import org.l2jdd.gameserver.model.items.type.WeaponType;
import org.l2jdd.gameserver.model.skills.ISkillCondition;
import org.l2jdd.gameserver.model.skills.Skill;

/**
 * @author UnAfraid
 */
public class OpTargetWeaponAttackTypeSkillCondition implements ISkillCondition
{
	private final List<WeaponType> _weaponTypes = new ArrayList<>();
	
	public OpTargetWeaponAttackTypeSkillCondition(StatSet params)
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
		if ((target == null) || !target.isCreature())
		{
			return false;
		}
		
		final Creature targetCreature = (Creature) target;
		final Weapon weapon = targetCreature.getActiveWeaponItem();
		if (weapon == null)
		{
			return false;
		}
		
		final WeaponType equippedType = weapon.getItemType();
		for (WeaponType weaponType : _weaponTypes)
		{
			if (weaponType == equippedType)
			{
				return true;
			}
		}
		
		return false;
	}
}
