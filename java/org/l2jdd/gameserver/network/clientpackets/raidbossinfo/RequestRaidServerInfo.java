
package org.l2jdd.gameserver.network.clientpackets.raidbossinfo;

import org.l2jdd.commons.network.PacketReader;
import org.l2jdd.gameserver.network.GameClient;
import org.l2jdd.gameserver.network.clientpackets.IClientIncomingPacket;

/**
 * @author Mobius
 */
public class RequestRaidServerInfo implements IClientIncomingPacket
{
	@Override
	public boolean read(GameClient client, PacketReader packet)
	{
		// System.out.println("RequestRaidServerInfo");
		return true;
	}
	
	@Override
	public void run(GameClient client)
	{
	}
}
