
package handlers.effecthandlers;

import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.actor.Creature;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.model.effects.AbstractEffect;
import org.l2jdd.gameserver.model.items.instance.ItemInstance;
import org.l2jdd.gameserver.model.skills.Skill;

/**
 * Item Effect: Decreases/resets karma count.
 * @author Nik
 */
public class KarmaCount extends AbstractEffect
{
	private final int _amount;
	private final int _mode;
	
	public KarmaCount(StatSet params)
	{
		_amount = params.getInt("amount", 0);
		switch (params.getString("mode", "DIFF"))
		{
			case "DIFF":
			{
				_mode = 0;
				break;
			}
			case "RESET":
			{
				_mode = 1;
				break;
			}
			default:
			{
				throw new IllegalArgumentException("Mode should be DIFF or RESET skill id:" + params.getInt("id"));
			}
		}
	}
	
	@Override
	public boolean isInstant()
	{
		return true;
	}
	
	@Override
	public void instant(Creature effector, Creature effected, Skill skill, ItemInstance item)
	{
		final PlayerInstance player = effected.getActingPlayer();
		if (player == null)
		{
			return;
		}
		
		// Check if player has no karma.
		if (player.getReputation() >= 0)
		{
			return;
		}
		
		switch (_mode)
		{
			case 0: // diff
			{
				final int newReputation = Math.min(player.getReputation() + _amount, 0);
				player.setReputation(newReputation);
				break;
			}
			case 1: // reset
			{
				player.setReputation(0);
			}
		}
	}
}
