
package handlers.effecthandlers;

import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.actor.Creature;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.model.effects.AbstractEffect;
import org.l2jdd.gameserver.model.events.EventDispatcher;
import org.l2jdd.gameserver.model.events.impl.creature.player.OnPlayerSummonAgathion;
import org.l2jdd.gameserver.model.items.instance.ItemInstance;
import org.l2jdd.gameserver.model.skills.Skill;
import org.l2jdd.gameserver.network.serverpackets.ExUserInfoCubic;

/**
 * Summon Agathion effect implementation.
 * @author Zoey76
 */
public class SummonAgathion extends AbstractEffect
{
	private final int _npcId;
	
	public SummonAgathion(StatSet params)
	{
		if (params.isEmpty())
		{
			LOGGER.warning(getClass().getSimpleName() + ": must have parameters.");
		}
		
		_npcId = params.getInt("npcId", 0);
	}
	
	@Override
	public boolean isInstant()
	{
		return true;
	}
	
	@Override
	public void instant(Creature effector, Creature effected, Skill skill, ItemInstance item)
	{
		if (!effected.isPlayer())
		{
			return;
		}
		
		final PlayerInstance player = effected.getActingPlayer();
		
		player.setAgathionId(_npcId);
		player.sendPacket(new ExUserInfoCubic(player));
		player.broadcastCharInfo();
		
		EventDispatcher.getInstance().notifyEventAsync(new OnPlayerSummonAgathion(player, _npcId));
	}
}
