
package handlers.effecthandlers;

import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.effects.AbstractEffect;
import org.l2jdd.gameserver.model.effects.EffectFlag;

/**
 * Block escape effect implementation
 * @author UnAfraid
 */
public class BlockEscape extends AbstractEffect
{
	public BlockEscape(StatSet params)
	{
	}
	
	@Override
	public long getEffectFlags()
	{
		return EffectFlag.CANNOT_ESCAPE.getMask();
	}
}
