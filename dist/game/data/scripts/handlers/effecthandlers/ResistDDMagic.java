
package handlers.effecthandlers;

import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.stats.Stat;

/**
 * @author Sdw
 */
public class ResistDDMagic extends AbstractStatPercentEffect
{
	public ResistDDMagic(StatSet params)
	{
		super(params, Stat.MAGIC_SUCCESS_RES);
	}
}
