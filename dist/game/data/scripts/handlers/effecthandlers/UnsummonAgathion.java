
package handlers.effecthandlers;

import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.actor.Creature;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.model.effects.AbstractEffect;
import org.l2jdd.gameserver.model.events.EventDispatcher;
import org.l2jdd.gameserver.model.events.impl.creature.player.OnPlayerUnsummonAgathion;
import org.l2jdd.gameserver.model.items.instance.ItemInstance;
import org.l2jdd.gameserver.model.skills.Skill;
import org.l2jdd.gameserver.network.serverpackets.ExUserInfoCubic;

/**
 * Unsummon Agathion effect implementation.
 * @author Zoey76
 */
public class UnsummonAgathion extends AbstractEffect
{
	public UnsummonAgathion(StatSet params)
	{
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
			final int agathionId = player.getAgathionId();
			if (agathionId > 0)
			{
				player.setAgathionId(0);
				player.sendPacket(new ExUserInfoCubic(player));
				player.broadcastCharInfo();
				
				EventDispatcher.getInstance().notifyEventAsync(new OnPlayerUnsummonAgathion(player, agathionId));
			}
		}
	}
}
