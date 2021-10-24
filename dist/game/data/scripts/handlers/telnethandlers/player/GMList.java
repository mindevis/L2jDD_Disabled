
package handlers.telnethandlers.player;

import org.l2jdd.Config;
import org.l2jdd.gameserver.data.xml.AdminData;
import org.l2jdd.gameserver.network.telnet.ITelnetCommand;

import io.netty.channel.ChannelHandlerContext;

/**
 * @author UnAfraid
 */
public class GMList implements ITelnetCommand
{
	@Override
	public String getCommand()
	{
		return "gmlist";
	}
	
	@Override
	public String getUsage()
	{
		return "GMList";
	}
	
	@Override
	public String handle(ChannelHandlerContext ctx, String[] args)
	{
		int i = 0;
		String gms = "";
		for (String player : AdminData.getInstance().getAllGmNames(true))
		{
			gms += player + ", ";
			i++;
		}
		if (!gms.isEmpty())
		{
			gms = gms.substring(0, gms.length() - 2) + ".";
		}
		return "There are currently " + i + " GM(s) online..." + Config.EOL + gms;
	}
}
