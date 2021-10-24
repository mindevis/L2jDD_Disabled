
package org.l2jdd.loginserver.network.loginserverpackets;

import org.l2jdd.commons.network.BaseSendablePacket;

/**
 * @author mrTJO
 */
public class RequestCharacters extends BaseSendablePacket
{
	public RequestCharacters(String account)
	{
		writeC(0x05);
		writeS(account);
	}
	
	@Override
	public byte[] getContent()
	{
		return getBytes();
	}
}
