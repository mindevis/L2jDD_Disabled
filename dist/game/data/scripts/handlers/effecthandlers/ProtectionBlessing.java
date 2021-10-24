
package handlers.effecthandlers;

import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.actor.Creature;
import org.l2jdd.gameserver.model.effects.AbstractEffect;
import org.l2jdd.gameserver.model.effects.EffectFlag;
import org.l2jdd.gameserver.model.skills.Skill;

/**
 * Protection Blessing effect implementation.
 * @author kerberos_20
 */
public class ProtectionBlessing extends AbstractEffect
{
	public ProtectionBlessing(StatSet params)
	{
	}
	
	@Override
	public boolean canStart(Creature effector, Creature effected, Skill skill)
	{
		return (effector != null) && (effected != null) && effected.isPlayer();
	}
	
	@Override
	public long getEffectFlags()
	{
		return EffectFlag.PROTECTION_BLESSING.getMask();
	}
}