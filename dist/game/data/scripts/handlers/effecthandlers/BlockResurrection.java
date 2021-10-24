
package handlers.effecthandlers;

import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.effects.AbstractEffect;
import org.l2jdd.gameserver.model.effects.EffectFlag;

/**
 * Block Resurrection effect implementation.
 * @author UnAfraid
 */
public class BlockResurrection extends AbstractEffect
{
	public BlockResurrection(StatSet params)
	{
	}
	
	@Override
	public long getEffectFlags()
	{
		return EffectFlag.BLOCK_RESURRECTION.getMask();
	}
}