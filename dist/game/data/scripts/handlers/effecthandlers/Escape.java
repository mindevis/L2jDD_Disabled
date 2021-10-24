
package handlers.effecthandlers;

import org.l2jdd.gameserver.enums.TeleportWhereType;
import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.actor.Creature;
import org.l2jdd.gameserver.model.effects.AbstractEffect;
import org.l2jdd.gameserver.model.effects.EffectType;
import org.l2jdd.gameserver.model.items.instance.ItemInstance;
import org.l2jdd.gameserver.model.skills.Skill;

/**
 * Escape effect implementation.
 * @author Adry_85
 */
public class Escape extends AbstractEffect
{
	private final TeleportWhereType _escapeType;
	
	public Escape(StatSet params)
	{
		_escapeType = params.getEnum("escapeType", TeleportWhereType.class, null);
	}
	
	@Override
	public EffectType getEffectType()
	{
		return EffectType.TELEPORT;
	}
	
	@Override
	public boolean isInstant()
	{
		return true;
	}
	
	@Override
	public boolean canStart(Creature effector, Creature effected, Skill skill)
	{
		// While affected by escape blocking effect you cannot use Blink or Scroll of Escape
		return super.canStart(effector, effected, skill) && !effected.cannotEscape();
	}
	
	@Override
	public void instant(Creature effector, Creature effected, Skill skill, ItemInstance item)
	{
		if (_escapeType != null)
		{
			effected.teleToLocation(_escapeType, null);
		}
	}
}
