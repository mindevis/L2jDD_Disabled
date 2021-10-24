
package org.l2jdd.gameserver.network.clientpackets.primeshop;

import org.l2jdd.commons.network.PacketReader;
import org.l2jdd.gameserver.network.GameClient;
import org.l2jdd.gameserver.network.clientpackets.IClientIncomingPacket;

/**
 * @author Gnacik, UnAfraid
 */
public class RequestBRRecentProductList implements IClientIncomingPacket
{
	@Override
	public boolean read(GameClient client, PacketReader packet)
	{
		return true;
	}
	
	@Override
	public void run(GameClient client)
	{
		// PlayerInstance player = client.getPlayer();
		// TODO: Implement it.
	}
}