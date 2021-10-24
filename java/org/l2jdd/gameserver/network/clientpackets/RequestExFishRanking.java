
package org.l2jdd.gameserver.network.clientpackets;

import org.l2jdd.commons.network.PacketReader;
import org.l2jdd.gameserver.network.GameClient;

/**
 * Format: (ch) just a trigger
 * @author -Wooden-
 */
public class RequestExFishRanking implements IClientIncomingPacket
{
	@Override
	public boolean read(GameClient client, PacketReader packet)
	{
		return true;
	}
	
	@Override
	public void run(GameClient client)
	{
		LOGGER.info("C5: RequestExFishRanking");
	}
}