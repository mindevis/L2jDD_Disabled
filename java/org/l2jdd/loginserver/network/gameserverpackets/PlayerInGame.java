
package org.l2jdd.loginserver.network.gameserverpackets;

import org.l2jdd.commons.network.BaseRecievePacket;
import org.l2jdd.loginserver.GameServerThread;

/**
 * @author -Wooden-
 */
public class PlayerInGame extends BaseRecievePacket
{
	/**
	 * @param decrypt
	 * @param server
	 */
	public PlayerInGame(byte[] decrypt, GameServerThread server)
	{
		super(decrypt);
		final int size = readH();
		for (int i = 0; i < size; i++)
		{
			final String account = readS();
			server.addAccountOnGameServer(account);
		}
	}
}
