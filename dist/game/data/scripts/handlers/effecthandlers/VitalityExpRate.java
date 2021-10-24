
package handlers.effecthandlers;

import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.actor.Creature;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.model.skills.Skill;
import org.l2jdd.gameserver.model.stats.Stat;
import org.l2jdd.gameserver.network.serverpackets.ExUserBoostStat;

/**
 * @author Mobius
 */
public class VitalityExpRate extends AbstractStatPercentEffect
{
	public VitalityExpRate(StatSet params)
	{
		super(params, Stat.VITALITY_EXP_RATE);
	}
	
	@Override
	public void pump(Creature effected, Skill skill)
	{
		effected.getStat().mergeMul(Stat.VITALITY_EXP_RATE, (_amount / 100) + 1);
		
		// Send exp bonus to player.
		final PlayerInstance player = effected.getActingPlayer();
		if (player != null)
		{
			player.sendPacket(new ExUserBoostStat(player));
		}
	}
}
