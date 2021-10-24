
package handlers.effecthandlers;

import org.l2jdd.commons.concurrent.ThreadPool;
import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.actor.Creature;
import org.l2jdd.gameserver.model.items.instance.ItemInstance;
import org.l2jdd.gameserver.model.skills.Skill;
import org.l2jdd.gameserver.model.stats.Stat;

/**
 * @author Sdw
 */
public class MaxMp extends AbstractStatEffect
{
	private final boolean _heal;
	
	public MaxMp(StatSet params)
	{
		super(params, Stat.MAX_MP);
		
		_heal = params.getBoolean("heal", false);
	}
	
	@Override
	public void continuousInstant(Creature effector, Creature effected, Skill skill, ItemInstance item)
	{
		if (_heal)
		{
			ThreadPool.schedule(() ->
			{
				switch (_mode)
				{
					case DIFF:
					{
						effected.setCurrentMp(effected.getCurrentMp() + _amount);
						break;
					}
					case PER:
					{
						effected.setCurrentMp(effected.getCurrentMp() + (effected.getMaxMp() * (_amount / 100)));
						break;
					}
				}
			}, 100);
		}
	}
}
