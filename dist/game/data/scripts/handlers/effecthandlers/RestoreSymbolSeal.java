
package handlers.effecthandlers;

import org.l2jdd.Config;
import org.l2jdd.gameserver.enums.StatModifierType;
import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.actor.Creature;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.model.effects.AbstractEffect;
import org.l2jdd.gameserver.model.items.instance.ItemInstance;
import org.l2jdd.gameserver.model.skills.Skill;

/**
 * @author NviX
 */
public class RestoreSymbolSeal extends AbstractEffect
{
	private final int _amount;
	private final StatModifierType _mode;
	
	public RestoreSymbolSeal(StatSet params)
	{
		_amount = params.getInt("amount", 0);
		_mode = params.getEnum("mode", StatModifierType.class, StatModifierType.PER);
	}
	
	@Override
	public boolean isInstant()
	{
		return true;
	}
	
	@Override
	public void instant(Creature effector, Creature effected, Skill skill, ItemInstance item)
	{
		if (effected.isDead() || effected.isDoor())
		{
			return;
		}
		
		final PlayerInstance player = effected.getActingPlayer();
		final int basicAmount = _amount;
		double amount = 0;
		switch (_mode)
		{
			case DIFF:
			{
				amount = Math.min(basicAmount, Config.MAX_SYMBOL_SEAL_POINTS - player.getSymbolSealPoints());
				break;
			}
			case PER:
			{
				amount = Math.min((Config.MAX_SYMBOL_SEAL_POINTS * basicAmount) / 100, Config.MAX_SYMBOL_SEAL_POINTS - player.getSymbolSealPoints());
				break;
			}
		}
		
		if (amount > 0)
		{
			final double newSymbolSealPoints = amount + effected.getActingPlayer().getSymbolSealPoints();
			player.setSymbolSealPoints((int) newSymbolSealPoints);
			player.updateSymbolSealSkills();
			player.sendSkillList();
			player.broadcastUserInfo();
		}
	}
}
