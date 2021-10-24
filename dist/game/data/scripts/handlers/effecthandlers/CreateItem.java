
package handlers.effecthandlers;

import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.actor.Creature;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.model.effects.AbstractEffect;
import org.l2jdd.gameserver.model.items.instance.ItemInstance;
import org.l2jdd.gameserver.model.skills.Skill;

/**
 * An effect that allows the player to create dwarven recipe items up to a certain level.
 * @author Nik
 */
public class CreateItem extends AbstractEffect
{
	private final int _recipeLevel;
	
	public CreateItem(StatSet params)
	{
		_recipeLevel = params.getInt("value");
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
			player.setCreateItemLevel(_recipeLevel);
		}
	}
	
	@Override
	public void onExit(Creature effector, Creature effected, Skill skill)
	{
		final PlayerInstance player = effected.getActingPlayer();
		if (player != null)
		{
			player.setCreateItemLevel(0);
		}
	}
}