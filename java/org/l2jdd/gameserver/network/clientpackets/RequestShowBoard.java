
package org.l2jdd.gameserver.network.clientpackets;

import org.l2jdd.Config;
import org.l2jdd.commons.network.PacketReader;
import org.l2jdd.gameserver.handler.CommunityBoardHandler;
import org.l2jdd.gameserver.network.GameClient;

/**
 * RequestShowBoard client packet implementation.
 * @author Zoey76
 */
public class RequestShowBoard implements IClientIncomingPacket
{
	@SuppressWarnings("unused")
	private int _unknown;
	
	@Override
	public boolean read(GameClient client, PacketReader packet)
	{
		_unknown = packet.readD();
		return true;
	}
	
	@Override
	public void run(GameClient client)
	{
		CommunityBoardHandler.getInstance().handleParseCommand(Config.BBS_DEFAULT, client.getPlayer());
	}
}
