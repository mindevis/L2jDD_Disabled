
package handlers.effecthandlers;

import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.actor.Creature;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.model.effects.AbstractEffect;
import org.l2jdd.gameserver.model.items.instance.ItemInstance;
import org.l2jdd.gameserver.model.skills.Skill;
import org.l2jdd.gameserver.network.serverpackets.SkillCoolTime;

/**
 * @author Mobius
 */
public class ReuseSkillById extends AbstractEffect
{
	private final int _skillId;
	private final int _amount;
	
	public ReuseSkillById(StatSet params)
	{
		_skillId = params.getInt("skillId", 0);
		_amount = params.getInt("amount", 0);
	}
	
	@Override
	public boolean isInstant()
	{
		return true;
	}
	
	@Override
	public void instant(Creature effector, Creature effected, Skill skill, ItemInstance item)
	{
		final PlayerInstance player = effector.getActingPlayer();
		if (player != null)
		{
			final Skill s = player.getKnownSkill(_skillId);
			if (s != null)
			{
				if (_amount > 0)
				{
					final long reuse = player.getSkillRemainingReuseTime(s.getReuseHashCode());
					if (reuse > 0)
					{
						player.removeTimeStamp(s);
						player.addTimeStamp(s, Math.max(0, reuse - _amount));
						player.sendPacket(new SkillCoolTime(player));
					}
				}
				else
				{
					player.removeTimeStamp(s);
					player.enableSkill(s);
					player.sendPacket(new SkillCoolTime(player));
				}
			}
		}
	}
}
