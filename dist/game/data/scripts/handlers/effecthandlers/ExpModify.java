
package handlers.effecthandlers;

import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.actor.Creature;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.model.skills.Skill;
import org.l2jdd.gameserver.model.stats.Stat;
import org.l2jdd.gameserver.network.serverpackets.ExUserBoostStat;

/**
 * @author Sdw, Mobius
 */
public class ExpModify extends AbstractStatAddEffect
{
	public ExpModify(StatSet params)
	{
		super(params, Stat.BONUS_EXP);
	}
	
	@Override
	public void pump(Creature effected, Skill skill)
	{
		effected.getStat().mergeAdd(Stat.BONUS_EXP, _amount);
		
		// Send exp bonus to player.
		final PlayerInstance player = effected.getActingPlayer();
		if (player != null)
		{
			player.sendPacket(new ExUserBoostStat(player));
		}
	}
}
