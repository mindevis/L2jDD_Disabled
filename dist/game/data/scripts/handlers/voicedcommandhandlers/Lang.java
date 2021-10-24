
package handlers.voicedcommandhandlers;

import java.util.StringTokenizer;

import org.l2jdd.Config;
import org.l2jdd.commons.concurrent.ThreadPool;
import org.l2jdd.gameserver.data.xml.NpcNameLocalisationData;
import org.l2jdd.gameserver.handler.IVoicedCommandHandler;
import org.l2jdd.gameserver.model.World;
import org.l2jdd.gameserver.model.WorldObject;
import org.l2jdd.gameserver.model.actor.Npc;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.network.serverpackets.DeleteObject;
import org.l2jdd.gameserver.network.serverpackets.NpcHtmlMessage;
import org.l2jdd.gameserver.network.serverpackets.NpcInfo;

public class Lang implements IVoicedCommandHandler
{
	private static final String[] VOICED_COMMANDS =
	{
		"lang"
	};
	
	@Override
	public boolean useVoicedCommand(String command, PlayerInstance activeChar, String params)
	{
		if (!Config.MULTILANG_ENABLE || !Config.MULTILANG_VOICED_ALLOW)
		{
			return false;
		}
		
		final NpcHtmlMessage msg = new NpcHtmlMessage();
		if (params == null)
		{
			final StringBuilder html = new StringBuilder(100);
			for (String lang : Config.MULTILANG_ALLOWED)
			{
				html.append("<button value=\"" + lang.toUpperCase() + "\" action=\"bypass -h voice .lang " + lang + "\" width=60 height=21 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"><br>");
			}
			
			msg.setFile(activeChar, "data/html/mods/Lang/LanguageSelect.htm");
			msg.replace("%list%", html.toString());
			activeChar.sendPacket(msg);
			return true;
		}
		
		final StringTokenizer st = new StringTokenizer(params);
		if (st.hasMoreTokens())
		{
			final String lang = st.nextToken().trim();
			if (activeChar.setLang(lang))
			{
				msg.setFile(activeChar, "data/html/mods/Lang/Ok.htm");
				activeChar.sendPacket(msg);
				for (WorldObject obj : World.getInstance().getVisibleObjects())
				{
					if (obj.isNpc() && NpcNameLocalisationData.getInstance().hasLocalisation(obj.getId()))
					{
						activeChar.sendPacket(new DeleteObject(obj));
						ThreadPool.schedule(() -> activeChar.sendPacket(new NpcInfo((Npc) obj)), 1000);
					}
				}
				activeChar.setTarget(null);
				return true;
			}
			msg.setFile(activeChar, "data/html/mods/Lang/Error.htm");
			activeChar.sendPacket(msg);
			return true;
		}
		return false;
	}
	
	@Override
	public String[] getVoicedCommandList()
	{
		return VOICED_COMMANDS;
	}
}