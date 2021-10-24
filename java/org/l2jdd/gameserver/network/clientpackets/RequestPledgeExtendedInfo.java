
package org.l2jdd.gameserver.network.clientpackets;

import org.l2jdd.commons.network.PacketReader;
import org.l2jdd.gameserver.network.GameClient;

/**
 * Format: (c) S S: pledge name?
 * @author -Wooden-
 */
public class RequestPledgeExtendedInfo implements IClientIncomingPacket
{
	@SuppressWarnings("unused")
	private String _name;
	
	@Override
	public boolean read(GameClient client, PacketReader packet)
	{
		_name = packet.readS();
		return true;
	}
	
	@Override
	public void run(GameClient client)
	{
		// TODO: Implement
	}
}
