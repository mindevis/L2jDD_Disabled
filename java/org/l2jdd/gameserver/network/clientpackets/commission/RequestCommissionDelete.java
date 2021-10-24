
package org.l2jdd.gameserver.network.clientpackets.commission;

import org.l2jdd.commons.network.PacketReader;
import org.l2jdd.gameserver.instancemanager.CommissionManager;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.network.GameClient;
import org.l2jdd.gameserver.network.clientpackets.IClientIncomingPacket;
import org.l2jdd.gameserver.network.serverpackets.commission.ExCloseCommission;

/**
 * @author NosBit
 */
public class RequestCommissionDelete implements IClientIncomingPacket
{
	private long _commissionId;
	
	@Override
	public boolean read(GameClient client, PacketReader packet)
	{
		_commissionId = packet.readQ();
		// packet.readD(); // CommissionItemType
		// packet.readD(); // CommissionDurationType
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
		
		if (!CommissionManager.isPlayerAllowedToInteract(player))
		{
			client.sendPacket(ExCloseCommission.STATIC_PACKET);
			return;
		}
		
		CommissionManager.getInstance().deleteItem(player, _commissionId);
	}
}
