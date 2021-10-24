
package org.l2jdd.gameserver.network.clientpackets;

import org.l2jdd.commons.network.PacketReader;
import org.l2jdd.gameserver.enums.UserInfoType;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.network.GameClient;

/**
 * @author Sdw
 */
public class NotifyExitBeautyShop implements IClientIncomingPacket
{
	@Override
	public boolean read(GameClient client, PacketReader packet)
	{
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
		
		player.broadcastUserInfo(UserInfoType.APPAREANCE);
	}
}
