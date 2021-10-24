
package handlers.effecthandlers;

import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.actor.Creature;
import org.l2jdd.gameserver.model.effects.AbstractEffect;
import org.l2jdd.gameserver.model.items.instance.ItemInstance;
import org.l2jdd.gameserver.model.skills.Skill;
import org.l2jdd.gameserver.network.serverpackets.UserInfo;

/**
 * Vitality Point Up effect implementation.
 * @author Adry_85
 */
public class VitalityPointUp extends AbstractEffect
{
	private final int _value;
	
	public VitalityPointUp(StatSet params)
	{
		_value = params.getInt("value", 0);
	}
	
	@Override
	public boolean canStart(Creature effector, Creature effected, Skill skill)
	{
		return (effected != null) && effected.isPlayer();
	}
	
	@Override
	public boolean isInstant()
	{
		return true;
	}
	
	@Override
	public void instant(Creature effector, Creature effected, Skill skill, ItemInstance item)
	{
		effected.getActingPlayer().updateVitalityPoints(_value, false, false);
		effected.getActingPlayer().sendPacket(new UserInfo(effected.getActingPlayer()));
	}
}
