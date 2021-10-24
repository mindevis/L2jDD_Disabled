
package handlers.effecthandlers;

import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.stats.Stat;

/**
 * @author Sdw
 */
public class MagicalAbnormalResist extends AbstractStatAddEffect
{
	public MagicalAbnormalResist(StatSet params)
	{
		super(params, Stat.ABNORMAL_RESIST_MAGICAL);
	}
}
