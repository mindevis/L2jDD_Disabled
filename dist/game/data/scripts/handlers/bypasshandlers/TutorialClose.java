
package handlers.bypasshandlers;

import org.l2jdd.gameserver.enums.HtmlActionScope;
import org.l2jdd.gameserver.handler.IBypassHandler;
import org.l2jdd.gameserver.model.actor.Creature;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.network.serverpackets.TutorialCloseHtml;

/**
 * @author UnAfraid
 */
public class TutorialClose implements IBypassHandler
{
	private static final String[] COMMANDS =
	{
		"tutorial_close",
	};
	
	@Override
	public boolean useBypass(String command, PlayerInstance player, Creature target)
	{
		player.sendPacket(TutorialCloseHtml.STATIC_PACKET);
		player.clearHtmlActions(HtmlActionScope.TUTORIAL_HTML);
		return false;
	}
	
	@Override
	public String[] getBypassList()
	{
		return COMMANDS;
	}
}
