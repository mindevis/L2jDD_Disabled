
package handlers.skillconditionhandlers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.WorldObject;
import org.l2jdd.gameserver.model.actor.Creature;
import org.l2jdd.gameserver.model.actor.Summon;
import org.l2jdd.gameserver.model.skills.ISkillCondition;
import org.l2jdd.gameserver.model.skills.Skill;

/**
 * @author UnAfraid
 */
public class OpNeedSummonOrPetSkillCondition implements ISkillCondition
{
	private final List<Integer> _npcIds = new ArrayList<>();
	
	public OpNeedSummonOrPetSkillCondition(StatSet params)
	{
		final List<String> npcIds = params.getList("npcIds", String.class);
		if (npcIds != null)
		{
			npcIds.stream().map(Integer::valueOf).forEach(_npcIds::add);
		}
	}
	
	@Override
	public boolean canUse(Creature caster, Skill skill, WorldObject target)
	{
		final Summon pet = caster.getPet();
		final Collection<Summon> summons = caster.getServitors().values();
		return ((pet != null) && _npcIds.stream().anyMatch(npcId -> npcId == pet.getId())) || summons.stream().anyMatch(summon -> _npcIds.contains(summon.getId()));
	}
}
