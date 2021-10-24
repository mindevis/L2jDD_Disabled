
package handlers.effecthandlers;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.actor.Creature;
import org.l2jdd.gameserver.model.effects.AbstractEffect;
import org.l2jdd.gameserver.model.effects.EffectType;
import org.l2jdd.gameserver.model.items.instance.ItemInstance;
import org.l2jdd.gameserver.model.skills.AbnormalType;
import org.l2jdd.gameserver.model.skills.Skill;

/**
 * Dispel By Slot effect implementation.
 * @author Gnacik, Zoey76, Adry_85
 */
public class DispelBySlotMyself extends AbstractEffect
{
	private final Set<AbnormalType> _dispelAbnormals;
	
	public DispelBySlotMyself(StatSet params)
	{
		final String dispel = params.getString("dispel");
		if ((dispel != null) && !dispel.isEmpty())
		{
			_dispelAbnormals = new HashSet<>();
			for (String slot : dispel.split(";"))
			{
				_dispelAbnormals.add(AbnormalType.getAbnormalType(slot));
			}
		}
		else
		{
			_dispelAbnormals = Collections.<AbnormalType> emptySet();
		}
	}
	
	@Override
	public EffectType getEffectType()
	{
		return EffectType.DISPEL_BY_SLOT;
	}
	
	@Override
	public boolean isInstant()
	{
		return true;
	}
	
	@Override
	public void instant(Creature effector, Creature effected, Skill skill, ItemInstance item)
	{
		if (_dispelAbnormals.isEmpty())
		{
			return;
		}
		
		// The effectlist should already check if it has buff with this abnormal type or not.
		effected.getEffectList().stopEffects(info -> !info.getSkill().isIrreplacableBuff() && _dispelAbnormals.contains(info.getSkill().getAbnormalType()), true, true);
	}
}
