
package handlers.skillconditionhandlers;

import java.util.List;

import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.World;
import org.l2jdd.gameserver.model.WorldObject;
import org.l2jdd.gameserver.model.actor.Creature;
import org.l2jdd.gameserver.model.actor.Npc;
import org.l2jdd.gameserver.model.skills.ISkillCondition;
import org.l2jdd.gameserver.model.skills.Skill;

/**
 * @author UnAfraid
 */
public class OpExistNpcSkillCondition implements ISkillCondition
{
	private final List<Integer> _npcIds;
	private final int _range;
	private final boolean _isAround;
	
	public OpExistNpcSkillCondition(StatSet params)
	{
		_npcIds = params.getList("npcIds", Integer.class);
		_range = params.getInt("range");
		_isAround = params.getBoolean("isAround");
	}
	
	@Override
	public boolean canUse(Creature caster, Skill skill, WorldObject target)
	{
		final List<Npc> npcs = World.getInstance().getVisibleObjectsInRange(caster, Npc.class, _range);
		return _isAround == npcs.stream().anyMatch(npc -> _npcIds.contains(npc.getId()));
	}
}
