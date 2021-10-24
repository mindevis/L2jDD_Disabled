
package handlers.effecthandlers;

import org.l2jdd.gameserver.enums.ItemGrade;
import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.actor.Creature;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.model.effects.AbstractEffect;
import org.l2jdd.gameserver.model.items.instance.ItemInstance;
import org.l2jdd.gameserver.model.skills.Skill;

/**
 * An effect that allows the player to crystallize items up to a certain grade.
 * @author Nik
 */
public class Crystallize extends AbstractEffect
{
	private final ItemGrade _grade;
	
	public Crystallize(StatSet params)
	{
		_grade = params.getEnum("grade", ItemGrade.class);
	}
	
	@Override
	public boolean canStart(Creature effector, Creature effected, Skill skill)
	{
		return effected.isPlayer();
	}
	
	@Override
	public void onStart(Creature effector, Creature effected, Skill skill, ItemInstance item)
	{
		final PlayerInstance player = effected.getActingPlayer();
		if (player != null)
		{
			player.setCrystallizeGrade(_grade);
		}
	}
	
	@Override
	public void onExit(Creature effector, Creature effected, Skill skill)
	{
		final PlayerInstance player = effected.getActingPlayer();
		if (player != null)
		{
			player.setCrystallizeGrade(null);
		}
	}
}