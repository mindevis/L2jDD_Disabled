
package handlers.effecthandlers;

import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.effects.AbstractEffect;
import org.l2jdd.gameserver.model.effects.EffectFlag;

/**
 * Effect that blocks damage and heals to HP/MP.<br>
 * Regeneration or DOT shouldn't be blocked, Vampiric Rage and Balance Life as well.
 * @author Nik
 */
public class DamageBlock extends AbstractEffect
{
	private final boolean _blockHp;
	private final boolean _blockMp;
	
	public DamageBlock(StatSet params)
	{
		final String type = params.getString("type", null);
		_blockHp = type.equalsIgnoreCase("BLOCK_HP");
		_blockMp = type.equalsIgnoreCase("BLOCK_MP");
	}
	
	@Override
	public long getEffectFlags()
	{
		return _blockHp ? EffectFlag.HP_BLOCK.getMask() : (_blockMp ? EffectFlag.MP_BLOCK.getMask() : EffectFlag.NONE.getMask());
	}
}
