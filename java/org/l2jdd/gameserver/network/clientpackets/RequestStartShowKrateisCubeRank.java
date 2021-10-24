
package org.l2jdd.gameserver.network.clientpackets;

import org.l2jdd.commons.network.PacketReader;
import org.l2jdd.gameserver.network.GameClient;

/**
 * @author Mobius
 */
public class RequestStartShowKrateisCubeRank implements IClientIncomingPacket
{
	@Override
	public boolean read(GameClient client, PacketReader packet)
	{
		return false;
	}
	
	@Override
	public void run(GameClient client)
	{
		// TODO: Implement.
		System.out.println("RequestStartShowKrateisCubeRank");
	}
}
