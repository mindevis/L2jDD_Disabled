
package handlers.skillconditionhandlers;

import java.util.List;

import org.l2jdd.gameserver.enums.SkillConditionAffectType;
import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.WorldObject;
import org.l2jdd.gameserver.model.actor.Creature;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.model.skills.ISkillCondition;
import org.l2jdd.gameserver.model.skills.Skill;

/**
 * @author Mobius
 */
public class OpCheckSkillListSkillCondition implements ISkillCondition
{
	private final List<Integer> _skillIds;
	private final SkillConditionAffectType _affectType;
	
	public OpCheckSkillListSkillCondition(StatSet params)
	{
		_skillIds = params.getList("skillIds", Integer.class);
		_affectType = params.getEnum("affectType", SkillConditionAffectType.class);
	}
	
	@Override
	public boolean canUse(Creature caster, Skill skill, WorldObject target)
	{
		switch (_affectType)
		{
			case CASTER:
			{
				for (int id : _skillIds)
				{
					if (caster.getSkillLevel(id) > 0)
					{
						return true;
					}
				}
				break;
			}
			case TARGET:
			{
				if ((target != null) && !target.isPlayer())
				{
					final PlayerInstance player = target.getActingPlayer();
					for (int id : _skillIds)
					{
						if (player.getSkillLevel(id) > 0)
						{
							return true;
						}
					}
				}
				break;
			}
		}
		return false;
	}
}
