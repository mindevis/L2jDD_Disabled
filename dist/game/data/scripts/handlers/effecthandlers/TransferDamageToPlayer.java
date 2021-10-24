
package handlers.effecthandlers;

import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.actor.Creature;
import org.l2jdd.gameserver.model.actor.Playable;
import org.l2jdd.gameserver.model.items.instance.ItemInstance;
import org.l2jdd.gameserver.model.skills.Skill;
import org.l2jdd.gameserver.model.stats.Stat;

/**
 * Transfer Damage effect implementation.
 * @author UnAfraid
 */
public class TransferDamageToPlayer extends AbstractStatAddEffect
{
	public TransferDamageToPlayer(StatSet params)
	{
		super(params, Stat.TRANSFER_DAMAGE_TO_PLAYER);
	}
	
	@Override
	public void onExit(Creature effector, Creature effected, Skill skill)
	{
		if (effected.isPlayable() && effector.isPlayer())
		{
			((Playable) effected).setTransferDamageTo(null);
		}
	}
	
	@Override
	public void onStart(Creature effector, Creature effected, Skill skill, ItemInstance item)
	{
		if (effected.isPlayable() && effector.isPlayer())
		{
			((Playable) effected).setTransferDamageTo(effector.getActingPlayer());
		}
	}
}