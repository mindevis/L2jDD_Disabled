
package org.l2jdd.gameserver.network.clientpackets;

import org.l2jdd.commons.network.PacketReader;
import org.l2jdd.gameserver.instancemanager.DuelManager;
import org.l2jdd.gameserver.network.GameClient;

/**
 * Format:(ch) just a trigger
 * @author -Wooden-
 */
public class RequestDuelSurrender implements IClientIncomingPacket
{
	@Override
	public boolean read(GameClient client, PacketReader packet)
	{
		return true;
	}
	
	@Override
	public void run(GameClient client)
	{
		DuelManager.getInstance().doSurrender(client.getPlayer());
	}
}
