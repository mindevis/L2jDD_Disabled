
package org.l2jdd.gameserver.network.loginserverpackets.game;

import org.l2jdd.commons.network.BaseSendablePacket;

/**
 * @author -Wooden-
 */
public class PlayerLogout extends BaseSendablePacket
{
	public PlayerLogout(String player)
	{
		writeC(0x03);
		writeS(player);
	}
	
	@Override
	public byte[] getContent()
	{
		return getBytes();
	}
}