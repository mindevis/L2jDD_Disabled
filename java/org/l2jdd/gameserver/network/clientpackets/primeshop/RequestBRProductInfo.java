
package org.l2jdd.gameserver.network.clientpackets.primeshop;

import org.l2jdd.commons.network.PacketReader;
import org.l2jdd.gameserver.data.xml.PrimeShopData;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.network.GameClient;
import org.l2jdd.gameserver.network.clientpackets.IClientIncomingPacket;

/**
 * @author Gnacik, UnAfraid
 */
public class RequestBRProductInfo implements IClientIncomingPacket
{
	private int _brId;
	
	@Override
	public boolean read(GameClient client, PacketReader packet)
	{
		_brId = packet.readD();
		return true;
	}
	
	@Override
	public void run(GameClient client)
	{
		final PlayerInstance player = client.getPlayer();
		if (player != null)
		{
			PrimeShopData.getInstance().showProductInfo(player, _brId);
		}
	}
}
