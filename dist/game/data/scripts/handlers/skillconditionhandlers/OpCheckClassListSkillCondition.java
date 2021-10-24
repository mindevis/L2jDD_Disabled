
package handlers.skillconditionhandlers;

import java.util.List;

import org.l2jdd.gameserver.enums.ClassId;
import org.l2jdd.gameserver.enums.SkillConditionAffectType;
import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.WorldObject;
import org.l2jdd.gameserver.model.actor.Creature;
import org.l2jdd.gameserver.model.skills.ISkillCondition;
import org.l2jdd.gameserver.model.skills.Skill;

/**
 * @author UnAfraid
 */
public class OpCheckClassListSkillCondition implements ISkillCondition
{
	private final List<ClassId> _classIds;
	private final SkillConditionAffectType _affectType;
	private final boolean _isWithin;
	
	public OpCheckClassListSkillCondition(StatSet params)
	{
		_classIds = params.getEnumList("classIds", ClassId.class);
		_affectType = params.getEnum("affectType", SkillConditionAffectType.class);
		_isWithin = params.getBoolean("isWithin");
	}
	
	@Override
	public boolean canUse(Creature caster, Skill skill, WorldObject target)
	{
		switch (_affectType)
		{
			case CASTER:
			{
				return caster.isPlayer() && (_isWithin == _classIds.stream().anyMatch(classId -> classId == caster.getActingPlayer().getClassId()));
			}
			case TARGET:
			{
				if ((target != null) && target.isPlayer())
				{
					return _isWithin == _classIds.stream().anyMatch(classId -> classId.getId() == target.getActingPlayer().getClassId().getId());
				}
				break;
			}
		}
		return false;
	}
}
