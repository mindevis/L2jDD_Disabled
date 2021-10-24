
package org.l2jdd.gameserver.network.clientpackets;

import org.l2jdd.commons.network.PacketReader;
import org.l2jdd.gameserver.network.GameClient;

/**
 * @author zabbix Lets drink to code!
 */
public class DummyPacket implements IClientIncomingPacket
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
