
package handlers.skillconditionhandlers;

import org.l2jdd.gameserver.enums.SkillConditionAffectType;
import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.WorldObject;
import org.l2jdd.gameserver.model.actor.Creature;
import org.l2jdd.gameserver.model.items.instance.ItemInstance;
import org.l2jdd.gameserver.model.skills.ISkillCondition;
import org.l2jdd.gameserver.model.skills.Skill;

/**
 * @author Mobius
 */
public class OpEquipItemSkillCondition implements ISkillCondition
{
	private final int _itemId;
	private final SkillConditionAffectType _affectType;
	
	public OpEquipItemSkillCondition(StatSet params)
	{
		_itemId = params.getInt("itemId");
		_affectType = params.getEnum("affectType", SkillConditionAffectType.class);
	}
	
	@Override
	public boolean canUse(Creature caster, Skill skill, WorldObject target)
	{
		switch (_affectType)
		{
			case CASTER:
			{
				for (ItemInstance item : caster.getInventory().getPaperdollItems())
				{
					if (item.getId() == _itemId)
					{
						return true;
					}
				}
				return false;
			}
			case TARGET:
			{
				if ((target != null) && target.isPlayer())
				{
					for (ItemInstance item : target.getActingPlayer().getInventory().getPaperdollItems())
					{
						if (item.getId() == _itemId)
						{
							return true;
						}
					}
				}
				return false;
			}
			case BOTH:
			{
				if ((target != null) && target.isPlayer())
				{
					for (ItemInstance item : caster.getInventory().getPaperdollItems())
					{
						if (item.getId() == _itemId)
						{
							for (ItemInstance i : target.getActingPlayer().getInventory().getPaperdollItems())
							{
								if (i.getId() == _itemId)
								{
									return true;
								}
							}
							return false;
						}
					}
				}
				return false;
			}
		}
		return false;
	}
}
