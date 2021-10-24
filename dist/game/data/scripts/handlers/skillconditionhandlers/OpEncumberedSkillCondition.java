
package handlers.skillconditionhandlers;

import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.WorldObject;
import org.l2jdd.gameserver.model.actor.Creature;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.model.skills.ISkillCondition;
import org.l2jdd.gameserver.model.skills.Skill;

/**
 * TODO: Verify me, also should Quest items be counted?
 * @author UnAfraid
 */
public class OpEncumberedSkillCondition implements ISkillCondition
{
	private final int _slotsPercent;
	private final int _weightPercent;
	
	public OpEncumberedSkillCondition(StatSet params)
	{
		_slotsPercent = params.getInt("slotsPercent");
		_weightPercent = params.getInt("weightPercent");
	}
	
	@Override
	public boolean canUse(Creature caster, Skill skill, WorldObject target)
	{
		if (!caster.isPlayer())
		{
			return false;
		}
		
		final PlayerInstance player = caster.getActingPlayer();
		final int currentSlotsPercent = calcPercent(player.getInventoryLimit(), player.getInventory().getSize(item -> !item.isQuestItem()));
		final int currentWeightPercent = calcPercent(player.getMaxLoad(), player.getCurrentLoad());
		return (currentSlotsPercent >= _slotsPercent) && (currentWeightPercent >= _weightPercent);
	}
	
	private int calcPercent(int max, int current)
	{
		return 100 - ((current * 100) / max);
	}
}
