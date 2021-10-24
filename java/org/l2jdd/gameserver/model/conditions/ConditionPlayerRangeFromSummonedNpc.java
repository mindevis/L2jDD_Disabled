
package org.l2jdd.gameserver.model.conditions;

import org.l2jdd.commons.util.CommonUtil;
import org.l2jdd.gameserver.model.World;
import org.l2jdd.gameserver.model.actor.Creature;
import org.l2jdd.gameserver.model.actor.Npc;
import org.l2jdd.gameserver.model.items.Item;
import org.l2jdd.gameserver.model.skills.Skill;

/**
 * Condition which checks if you are within the given range of a summoned by you npc.
 * @author Nik
 */
public class ConditionPlayerRangeFromSummonedNpc extends Condition
{
	/** NPC Ids. */
	private final int[] _npcIds;
	/** Radius to check. */
	private final int _radius;
	/** Expected value. */
	private final boolean _value;
	
	public ConditionPlayerRangeFromSummonedNpc(int[] npcIds, int radius, boolean value)
	{
		_npcIds = npcIds;
		_radius = radius;
		_value = value;
	}
	
	@Override
	public boolean testImpl(Creature effector, Creature effected, Skill skill, Item item)
	{
		boolean existNpc = false;
		if ((_npcIds != null) && (_npcIds.length > 0) && (_radius > 0))
		{
			for (Npc target : World.getInstance().getVisibleObjectsInRange(effector, Npc.class, _radius))
			{
				if (CommonUtil.contains(_npcIds, target.getId()) && (effector == target.getSummoner()))
				{
					existNpc = true;
					break;
				}
			}
		}
		return existNpc == _value;
	}
}
