
package handlers.effecthandlers;

import java.util.EnumMap;
import java.util.Map;
import java.util.Map.Entry;

import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.actor.Creature;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.model.effects.AbstractEffect;
import org.l2jdd.gameserver.model.skills.Skill;
import org.l2jdd.gameserver.model.stats.Stat;

/**
 * Servitor Share effect implementation.
 */
public class ServitorShare extends AbstractEffect
{
	private final Map<Stat, Float> _sharedStats = new EnumMap<>(Stat.class);
	
	public ServitorShare(StatSet params)
	{
		if (params.isEmpty())
		{
			return;
		}
		
		for (Entry<String, Object> param : params.getSet().entrySet())
		{
			_sharedStats.put(Stat.valueOf(param.getKey()), (Float.parseFloat((String) param.getValue())) / 100);
		}
	}
	
	@Override
	public boolean canPump(Creature effector, Creature effected, Skill skill)
	{
		return effected.isSummon();
	}
	
	@Override
	public void pump(Creature effected, Skill skill)
	{
		final PlayerInstance owner = effected.getActingPlayer();
		if (owner != null)
		{
			for (Entry<Stat, Float> stats : _sharedStats.entrySet())
			{
				effected.getStat().mergeAdd(stats.getKey(), owner.getStat().getValue(stats.getKey()) * stats.getValue().floatValue());
			}
		}
	}
}