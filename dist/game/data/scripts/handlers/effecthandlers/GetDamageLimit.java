
package handlers.effecthandlers;

import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.stats.Stat;

/**
 * @author Sdw
 */
public class GetDamageLimit extends AbstractStatAddEffect
{
	public GetDamageLimit(StatSet params)
	{
		super(params, Stat.DAMAGE_LIMIT);
	}
}
