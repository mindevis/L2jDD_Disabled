
package handlers.skillconditionhandlers;

import org.l2jdd.gameserver.geoengine.GeoEngine;
import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.WorldObject;
import org.l2jdd.gameserver.model.actor.Creature;
import org.l2jdd.gameserver.model.skills.ISkillCondition;
import org.l2jdd.gameserver.model.skills.Skill;

/**
 * @author Mobius
 */
public class OpCheckCastRangeSkillCondition implements ISkillCondition
{
	private final int _distance;
	
	public OpCheckCastRangeSkillCondition(StatSet params)
	{
		_distance = params.getInt("distance");
	}
	
	@Override
	public boolean canUse(Creature caster, Skill skill, WorldObject target)
	{
		return (target != null) //
			&& (caster.calculateDistance3D(target) >= _distance) //
			&& GeoEngine.getInstance().canSeeTarget(caster, target);
	}
}
