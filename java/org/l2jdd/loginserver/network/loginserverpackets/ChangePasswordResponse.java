
package org.l2jdd.loginserver.network.loginserverpackets;

import org.l2jdd.commons.network.BaseSendablePacket;

/**
 * @author Nik
 */
public class ChangePasswordResponse extends BaseSendablePacket
{
	public ChangePasswordResponse(byte successful, String characterName, String msgToSend)
	{
		writeC(0x06);
		// writeC(successful); // 0 false, 1 true
		writeS(characterName);
		writeS(msgToSend);
	}
	
	@Override
	public byte[] getContent()
	{
		return getBytes();
	}
}