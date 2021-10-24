
package org.l2jdd.gameserver.network.clientpackets.compound;

import org.l2jdd.commons.network.PacketReader;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.model.actor.request.CompoundRequest;
import org.l2jdd.gameserver.model.items.instance.ItemInstance;
import org.l2jdd.gameserver.network.GameClient;
import org.l2jdd.gameserver.network.SystemMessageId;
import org.l2jdd.gameserver.network.clientpackets.IClientIncomingPacket;
import org.l2jdd.gameserver.network.serverpackets.compound.ExEnchantOneFail;
import org.l2jdd.gameserver.network.serverpackets.compound.ExEnchantOneRemoveFail;
import org.l2jdd.gameserver.network.serverpackets.compound.ExEnchantOneRemoveOK;

/**
 * @author UnAfraid
 */
public class RequestNewEnchantRemoveOne implements IClientIncomingPacket
{
	private int _objectId;
	
	@Override
	public boolean read(GameClient client, PacketReader packet)
	{
		_objectId = packet.readD();
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
		else if (player.isInStoreMode())
		{
			client.sendPacket(SystemMessageId.YOU_CANNOT_DO_THAT_WHILE_IN_A_PRIVATE_STORE_OR_PRIVATE_WORKSHOP);
			client.sendPacket(ExEnchantOneFail.STATIC_PACKET);
			return;
		}
		else if (player.isProcessingTransaction() || player.isProcessingRequest())
		{
			client.sendPacket(SystemMessageId.YOU_CANNOT_USE_THIS_SYSTEM_DURING_TRADING_PRIVATE_STORE_AND_WORKSHOP_SETUP);
			client.sendPacket(ExEnchantOneFail.STATIC_PACKET);
			return;
		}
		
		final CompoundRequest request = player.getRequest(CompoundRequest.class);
		if ((request == null) || request.isProcessing())
		{
			client.sendPacket(ExEnchantOneRemoveFail.STATIC_PACKET);
			return;
		}
		
		final ItemInstance item = request.getItemOne();
		if ((item == null) || (item.getObjectId() != _objectId))
		{
			client.sendPacket(ExEnchantOneRemoveFail.STATIC_PACKET);
			return;
		}
		request.setItemOne(0);
		
		client.sendPacket(ExEnchantOneRemoveOK.STATIC_PACKET);
	}
}
