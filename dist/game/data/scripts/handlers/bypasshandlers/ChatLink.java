
package handlers.bypasshandlers;

import org.l2jdd.gameserver.handler.IBypassHandler;
import org.l2jdd.gameserver.model.actor.Creature;
import org.l2jdd.gameserver.model.actor.Npc;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.model.events.EventDispatcher;
import org.l2jdd.gameserver.model.events.EventType;
import org.l2jdd.gameserver.model.events.impl.creature.npc.OnNpcFirstTalk;

public class ChatLink implements IBypassHandler
{
	private static final String[] COMMANDS =
	{
		"Chat"
	};
	
	@Override
	public boolean useBypass(String command, PlayerInstance player, Creature target)
	{
		if (!target.isNpc())
		{
			return false;
		}
		
		int val = 0;
		try
		{
			val = Integer.parseInt(command.substring(5));
		}
		catch (Exception e)
		{
			// Handled above.
		}
		
		final Npc npc = (Npc) target;
		if ((val == 0) && npc.hasListener(EventType.ON_NPC_FIRST_TALK))
		{
			EventDispatcher.getInstance().notifyEventAsync(new OnNpcFirstTalk(npc, player), npc);
		}
		else
		{
			npc.showChatWindow(player, val);
		}
		return false;
	}
	
	@Override
	public String[] getBypassList()
	{
		return COMMANDS;
	}
}
