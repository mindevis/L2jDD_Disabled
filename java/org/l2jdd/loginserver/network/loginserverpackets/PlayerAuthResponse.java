
package org.l2jdd.loginserver.network.loginserverpackets;

import org.l2jdd.commons.network.BaseSendablePacket;

/**
 * @author -Wooden-
 */
public class PlayerAuthResponse extends BaseSendablePacket
{
	public PlayerAuthResponse(String account, boolean response)
	{
		writeC(0x03);
		writeS(account);
		writeC(response ? 1 : 0);
	}
	
	@Override
	public byte[] getContent()
	{
		return getBytes();
	}
}
