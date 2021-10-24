
package org.l2jdd.gameserver.network.loginserverpackets.login;

import org.l2jdd.commons.network.BaseRecievePacket;
import org.l2jdd.gameserver.model.World;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;

public class ChangePasswordResponse extends BaseRecievePacket
{
	public ChangePasswordResponse(byte[] decrypt)
	{
		super(decrypt);
		// boolean isSuccessful = readC() > 0;
		final String character = readS();
		final String msgToSend = readS();
		final PlayerInstance player = World.getInstance().getPlayer(character);
		if (player != null)
		{
			player.sendMessage(msgToSend);
		}
	}
}