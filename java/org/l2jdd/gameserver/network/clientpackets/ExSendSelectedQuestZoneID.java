
package org.l2jdd.gameserver.network.clientpackets;

import org.l2jdd.commons.network.PacketReader;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.network.GameClient;

/**
 * @author St3eT
 */
public class ExSendSelectedQuestZoneID implements IClientIncomingPacket
{
	private int _questZoneId;
	
	@Override
	public boolean read(GameClient client, PacketReader packet)
	{
		_questZoneId = packet.readD();
		return true;
	}
	
	@Override
	public void run(GameClient client)
	{
		final PlayerInstance player = client.getPlayer();
		if (player == null)
		{
			return;
		}
		
		player.setQuestZoneId(_questZoneId);
	}
}
