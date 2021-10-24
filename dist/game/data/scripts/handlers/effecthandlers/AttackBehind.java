
package handlers.effecthandlers;

import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.effects.AbstractEffect;
import org.l2jdd.gameserver.model.effects.EffectFlag;

/**
 * @author Nik
 */
public class AttackBehind extends AbstractEffect
{
	public AttackBehind(StatSet params)
	{
	}
	
	@Override
	public long getEffectFlags()
	{
		return EffectFlag.ATTACK_BEHIND.getMask();
	}
}
