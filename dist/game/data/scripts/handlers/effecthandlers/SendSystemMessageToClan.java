
package handlers.effecthandlers;

import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.actor.Creature;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.model.clan.Clan;
import org.l2jdd.gameserver.model.effects.AbstractEffect;
import org.l2jdd.gameserver.model.items.instance.ItemInstance;
import org.l2jdd.gameserver.model.skills.Skill;
import org.l2jdd.gameserver.network.serverpackets.SystemMessage;

/**
 * Item Effect: Increase/decrease PK count permanently.
 * @author Nik
 */
public class SendSystemMessageToClan extends AbstractEffect
{
	private final SystemMessage _message;
	
	public SendSystemMessageToClan(StatSet params)
	{
		final int id = params.getInt("id", 0);
		_message = new SystemMessage(id);
	}
	
	@Override
	public boolean isInstant()
	{
		return true;
	}
	
	@Override
	public void instant(Creature effector, Creature effected, Skill skill, ItemInstance item)
	{
		final PlayerInstance player = effected.getActingPlayer();
		if ((player == null) || (_message == null))
		{
			return;
		}
		
		final Clan clan = player.getClan();
		if (clan != null)
		{
			clan.broadcastToOnlineMembers(_message);
		}
	}
}
