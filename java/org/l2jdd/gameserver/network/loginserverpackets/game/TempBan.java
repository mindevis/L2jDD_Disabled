
package org.l2jdd.gameserver.network.loginserverpackets.game;

import org.l2jdd.commons.network.BaseSendablePacket;
import org.l2jdd.commons.util.Chronos;

/**
 * @author mrTJO
 */
public class TempBan extends BaseSendablePacket
{
	public TempBan(String accountName, String ip, long time)
	{
		writeC(0x0A);
		writeS(accountName);
		writeS(ip);
		writeQ(Chronos.currentTimeMillis() + (time * 60000));
		// if (reason != null)
		// {
		// writeC(0x01);
		// writeS(reason);
		// }
		// else
		// {
		writeC(0x00);
		// }
	}
	
	@Override
	public byte[] getContent()
	{
		return getBytes();
	}
}
