
package org.l2jdd.loginserver.network.loginserverpackets;

import org.l2jdd.commons.network.BaseSendablePacket;
import org.l2jdd.loginserver.GameServerTable;

/**
 * @author -Wooden-
 */
public class AuthResponse extends BaseSendablePacket
{
	/**
	 * @param serverId
	 */
	public AuthResponse(int serverId)
	{
		writeC(0x02);
		writeC(serverId);
		writeS(GameServerTable.getInstance().getServerNameById(serverId));
	}
	
	@Override
	public byte[] getContent()
	{
		return getBytes();
	}
}
