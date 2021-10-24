
package handlers.effecthandlers;

import org.l2jdd.commons.concurrent.ThreadPool;
import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.actor.Creature;
import org.l2jdd.gameserver.model.items.instance.ItemInstance;
import org.l2jdd.gameserver.model.skills.Skill;
import org.l2jdd.gameserver.model.stats.Stat;

/**
 * @author NosBit
 */
public class MaxHp extends AbstractStatEffect
{
	private final boolean _heal;
	
	public MaxHp(StatSet params)
	{
		super(params, Stat.MAX_HP);
		
		_heal = params.getBoolean("heal", false);
	}
	
	@Override
	public void continuousInstant(Creature effector, Creature effected, Skill skill, ItemInstance item)
	{
		if (_heal)
		{
			ThreadPool.schedule(() ->
			{
				if (!effected.isHpBlocked())
				{
					switch (_mode)
					{
						case DIFF:
						{
							effected.setCurrentHp(effected.getCurrentHp() + _amount);
							break;
						}
						case PER:
						{
							effected.setCurrentHp(effected.getCurrentHp() + (effected.getMaxHp() * (_amount / 100)));
							break;
						}
					}
				}
			}, 100);
		}
	}
}
