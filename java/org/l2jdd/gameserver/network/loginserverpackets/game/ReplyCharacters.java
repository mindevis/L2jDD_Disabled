
package org.l2jdd.gameserver.network.loginserverpackets.game;

import java.util.List;

import org.l2jdd.commons.network.BaseSendablePacket;

/**
 * @author mrTJO Thanks to mochitto
 */
public class ReplyCharacters extends BaseSendablePacket
{
	public ReplyCharacters(String account, int chars, List<Long> timeToDel)
	{
		writeC(0x08);
		writeS(account);
		writeC(chars);
		writeC(timeToDel.size());
		for (long time : timeToDel)
		{
			writeQ(time);
		}
	}
	
	@Override
	public byte[] getContent()
	{
		return getBytes();
	}
}
