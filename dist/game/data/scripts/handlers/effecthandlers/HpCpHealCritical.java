
package handlers.effecthandlers;

import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.effects.AbstractEffect;
import org.l2jdd.gameserver.model.effects.EffectFlag;

/**
 * @author Sdw
 */
public class HpCpHealCritical extends AbstractEffect
{
	public HpCpHealCritical(StatSet params)
	{
	}
	
	@Override
	public long getEffectFlags()
	{
		return EffectFlag.HPCPHEAL_CRITICAL.getMask();
	}
}
