
package org.l2jdd.gameserver.network.clientpackets.adenadistribution;

import org.l2jdd.commons.network.PacketReader;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.model.actor.request.AdenaDistributionRequest;
import org.l2jdd.gameserver.network.GameClient;
import org.l2jdd.gameserver.network.SystemMessageId;
import org.l2jdd.gameserver.network.clientpackets.IClientIncomingPacket;
import org.l2jdd.gameserver.network.serverpackets.adenadistribution.ExDivideAdenaCancel;

/**
 * @author Sdw
 */
public class RequestDivideAdenaCancel implements IClientIncomingPacket
{
	private boolean _cancel;
	
	@Override
	public boolean read(GameClient client, PacketReader packet)
	{
		_cancel = packet.readC() == 0;
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
		
		if (_cancel)
		{
			final AdenaDistributionRequest request = player.getRequest(AdenaDistributionRequest.class);
			for (PlayerInstance p : request.getPlayers())
			{
				if (p != null)
				{
					p.sendPacket(SystemMessageId.ADENA_DISTRIBUTION_HAS_BEEN_CANCELLED);
					p.sendPacket(ExDivideAdenaCancel.STATIC_PACKET);
					p.removeRequest(AdenaDistributionRequest.class);
				}
			}
		}
	}
}
