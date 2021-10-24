
package handlers.effecthandlers;

import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.effects.AbstractEffect;
import org.l2jdd.gameserver.model.effects.EffectFlag;

/**
 * @author Sdw
 */
public class ProtectDeathPenalty extends AbstractEffect
{
	public ProtectDeathPenalty(StatSet params)
	{
	}
	
	@Override
	public long getEffectFlags()
	{
		return EffectFlag.PROTECT_DEATH_PENALTY.getMask();
	}
}
