
package handlers.effecthandlers;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

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
public class DispelBySlot extends AbstractEffect
{
	private final String _dispel;
	private final Map<AbnormalType, Short> _dispelAbnormals;
	
	public DispelBySlot(StatSet params)
	{
		_dispel = params.getString("dispel");
		if ((_dispel != null) && !_dispel.isEmpty())
		{
			_dispelAbnormals = new EnumMap<>(AbnormalType.class);
			for (String ngtStack : _dispel.split(";"))
			{
				final String[] ngt = ngtStack.split(",");
				_dispelAbnormals.put(AbnormalType.getAbnormalType(ngt[0]), Short.parseShort(ngt[1]));
			}
		}
		else
		{
			_dispelAbnormals = Collections.<AbnormalType, Short> emptyMap();
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
		
		// Continue only if target has any of the abnormals. Save useless cycles.
		if (effected.getEffectList().hasAbnormalType(_dispelAbnormals.keySet()))
		{
			// Dispel transformations (buff and by GM)
			final Short transformToDispel = _dispelAbnormals.get(AbnormalType.TRANSFORM);
			if ((transformToDispel != null) && ((transformToDispel.intValue() == effected.getTransformationId()) || (transformToDispel.intValue() < 0)))
			{
				effected.stopTransformation(true);
			}
			
			effected.getEffectList().stopEffects(info ->
			{
				// We have already dealt with transformation from above.
				if (info.isAbnormalType(AbnormalType.TRANSFORM))
				{
					return false;
				}
				
				final Short abnormalLevel = _dispelAbnormals.get(info.getSkill().getAbnormalType());
				return (abnormalLevel != null) && ((abnormalLevel.shortValue() < 0) || (abnormalLevel.shortValue() >= info.getSkill().getAbnormalLevel()));
			}, true, true);
		}
	}
}
