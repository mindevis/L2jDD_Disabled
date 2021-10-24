
package org.l2jdd.gameserver.network.loginserverpackets.game;

import org.l2jdd.commons.network.BaseSendablePacket;

/**
 * @author mrTJO
 */
public class SendMail extends BaseSendablePacket
{
	public SendMail(String accountName, String mailId, String... args)
	{
		writeC(0x09);
		writeS(accountName);
		writeS(mailId);
		writeC(args.length);
		for (String arg : args)
		{
			writeS(arg);
		}
	}
	
	@Override
	public byte[] getContent()
	{
		return getBytes();
	}
}
