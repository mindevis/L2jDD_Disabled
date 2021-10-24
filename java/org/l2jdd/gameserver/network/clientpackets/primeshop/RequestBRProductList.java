
package org.l2jdd.gameserver.network.clientpackets.primeshop;

import org.l2jdd.commons.network.PacketReader;
import org.l2jdd.gameserver.data.xml.PrimeShopData;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.network.GameClient;
import org.l2jdd.gameserver.network.clientpackets.IClientIncomingPacket;
import org.l2jdd.gameserver.network.serverpackets.primeshop.ExBRProductList;

/**
 * @author Gnacik, UnAfraid
 */
public class RequestBRProductList implements IClientIncomingPacket
{
	private int _type;
	
	@Override
	public boolean read(GameClient client, PacketReader packet)
	{
		_type = packet.readD();
		return true;
	}
	
	@Override
	public void run(GameClient client)
	{
		final PlayerInstance player = client.getPlayer();
		if (player != null)
		{
			switch (_type)
			{
				case 0: // Home page
				{
					player.sendPacket(new ExBRProductList(player, 0, PrimeShopData.getInstance().getPrimeItems().values()));
					break;
				}
				case 1: // History
				{
					break;
				}
				case 2: // Favorites
				{
					break;
				}
				default:
				{
					LOGGER.warning(player + " send unhandled product list type: " + _type);
					break;
				}
			}
		}
	}
}