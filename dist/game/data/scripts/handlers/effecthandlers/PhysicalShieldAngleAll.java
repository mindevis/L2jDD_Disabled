
package handlers.effecthandlers;

import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.effects.AbstractEffect;
import org.l2jdd.gameserver.model.effects.EffectFlag;

/**
 * @author Sdw
 */
public class PhysicalShieldAngleAll extends AbstractEffect
{
	public PhysicalShieldAngleAll(StatSet params)
	{
	}
	
	@Override
	public long getEffectFlags()
	{
		return EffectFlag.PHYSICAL_SHIELD_ANGLE_ALL.getMask();
	}
}
