
package handlers.effecthandlers;

import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.stats.Stat;

/**
 * @author Sdw
 */
public class ReflectMagic extends AbstractStatAddEffect
{
	public ReflectMagic(StatSet params)
	{
		super(params, Stat.VENGEANCE_SKILL_MAGIC_DAMAGE);
	}
}
