
package handlers.effecthandlers;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.actor.Creature;
import org.l2jdd.gameserver.model.effects.AbstractEffect;
import org.l2jdd.gameserver.model.items.instance.ItemInstance;
import org.l2jdd.gameserver.model.skills.AbnormalType;
import org.l2jdd.gameserver.model.skills.BuffInfo;
import org.l2jdd.gameserver.model.skills.Skill;
import org.l2jdd.gameserver.network.serverpackets.AbnormalStatusUpdate;
import org.l2jdd.gameserver.network.serverpackets.ExAbnormalStatusUpdateFromTarget;

/**
 * @author Sdw
 */
public class AbnormalTimeChange extends AbstractEffect
{
	private final Set<AbnormalType> _abnormals;
	private final int _time;
	private final int _mode;
	
	public AbnormalTimeChange(StatSet params)
	{
		final String abnormals = params.getString("slot", null);
		if ((abnormals != null) && !abnormals.isEmpty())
		{
			_abnormals = new HashSet<>();
			for (String slot : abnormals.split(";"))
			{
				_abnormals.add(AbnormalType.getAbnormalType(slot));
			}
		}
		else
		{
			_abnormals = Collections.<AbnormalType> emptySet();
		}
		
		_time = params.getInt("time", -1);
		
		switch (params.getString("mode", "DEBUFF"))
		{
			case "DIFF":
			{
				_mode = 0;
				break;
			}
			case "DEBUFF":
			{
				_mode = 1;
				break;
			}
			default:
			{
				throw new IllegalArgumentException("Mode should be DIFF or DEBUFF for skill id:" + params.getInt("id"));
			}
		}
	}
	
	@Override
	public boolean isInstant()
	{
		return true;
	}
	
	@Override
	public void instant(Creature effector, Creature effected, Skill skill, ItemInstance item)
	{
		final AbnormalStatusUpdate asu = new AbnormalStatusUpdate();
		
		switch (_mode)
		{
			case 0: // DIFF
			{
				if (_abnormals.isEmpty())
				{
					for (BuffInfo info : effected.getEffectList().getEffects())
					{
						if (info.getSkill().canBeDispelled())
						{
							info.resetAbnormalTime(info.getTime() + _time);
							asu.addSkill(info);
						}
					}
				}
				else
				{
					for (BuffInfo info : effected.getEffectList().getEffects())
					{
						if (info.getSkill().canBeDispelled() && _abnormals.contains(info.getSkill().getAbnormalType()))
						{
							info.resetAbnormalTime(info.getTime() + _time);
							asu.addSkill(info);
						}
					}
				}
				break;
			}
			case 1: // DEBUFF
			{
				if (_abnormals.isEmpty())
				{
					for (BuffInfo info : effected.getEffectList().getDebuffs())
					{
						if (info.getSkill().canBeDispelled())
						{
							info.resetAbnormalTime(info.getAbnormalTime());
							asu.addSkill(info);
						}
					}
				}
				else
				{
					for (BuffInfo info : effected.getEffectList().getDebuffs())
					{
						if (info.getSkill().canBeDispelled() && _abnormals.contains(info.getSkill().getAbnormalType()))
						{
							info.resetAbnormalTime(info.getAbnormalTime());
							asu.addSkill(info);
						}
					}
				}
				break;
			}
		}
		
		effected.sendPacket(asu);
		
		final ExAbnormalStatusUpdateFromTarget upd = new ExAbnormalStatusUpdateFromTarget(effected);
		for (Creature creature : effected.getStatus().getStatusListener())
		{
			if ((creature != null) && creature.isPlayer())
			{
				upd.sendTo(creature.getActingPlayer());
			}
		}
		
		if (effected.isPlayer() && (effected.getTarget() == effected))
		{
			effected.sendPacket(upd);
		}
	}
}
