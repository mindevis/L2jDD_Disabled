
package handlers.effecthandlers;

import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.actor.Creature;
import org.l2jdd.gameserver.model.effects.AbstractEffect;
import org.l2jdd.gameserver.model.items.instance.ItemInstance;
import org.l2jdd.gameserver.model.skills.Skill;
import org.l2jdd.gameserver.network.SystemMessageId;
import org.l2jdd.gameserver.network.serverpackets.SystemMessage;

/**
 * Hp By Level effect implementation.
 * @author Zoey76
 */
public class HpByLevel extends AbstractEffect
{
	private final double _power;
	
	public HpByLevel(StatSet params)
	{
		_power = params.getDouble("power", 0);
	}
	
	@Override
	public boolean isInstant()
	{
		return true;
	}
	
	@Override
	public void instant(Creature effector, Creature effected, Skill skill, ItemInstance item)
	{
		// Calculation
		final double abs = _power;
		final double absorb = ((effector.getCurrentHp() + abs) > effector.getMaxHp() ? effector.getMaxHp() : (effector.getCurrentHp() + abs));
		final int restored = (int) (absorb - effector.getCurrentHp());
		effector.setCurrentHp(absorb);
		// System message
		final SystemMessage sm = new SystemMessage(SystemMessageId.S1_HP_HAS_BEEN_RESTORED);
		sm.addInt(restored);
		effector.sendPacket(sm);
	}
}
