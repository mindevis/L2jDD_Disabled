
package org.l2jdd.gameserver.network.clientpackets.commission;

import org.l2jdd.commons.network.PacketReader;
import org.l2jdd.gameserver.network.GameClient;
import org.l2jdd.gameserver.network.clientpackets.IClientIncomingPacket;

/**
 * This Packet doesn't seem to be doing anything.
 * @author NosBit
 */
public class RequestCommissionCancel implements IClientIncomingPacket
{
	@Override
	public boolean read(GameClient client, PacketReader packet)
	{
		return false;
	}
	
	@Override
	public void run(GameClient client)
	{
	}
}
