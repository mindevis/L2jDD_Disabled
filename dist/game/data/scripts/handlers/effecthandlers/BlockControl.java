
package handlers.effecthandlers;

import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.effects.AbstractEffect;
import org.l2jdd.gameserver.model.effects.EffectFlag;
import org.l2jdd.gameserver.model.effects.EffectType;

/**
 * An effect that blocks the player (NPC?) control.<br>
 * It prevents moving, casting, social actions, etc.
 * @author Nik
 */
public class BlockControl extends AbstractEffect
{
	public BlockControl(StatSet params)
	{
	}
	
	@Override
	public long getEffectFlags()
	{
		return EffectFlag.BLOCK_CONTROL.getMask();
	}
	
	@Override
	public EffectType getEffectType()
	{
		return EffectType.BLOCK_CONTROL;
	}
}
