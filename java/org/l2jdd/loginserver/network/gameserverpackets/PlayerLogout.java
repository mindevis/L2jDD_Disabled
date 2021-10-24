
package org.l2jdd.loginserver.network.gameserverpackets;

import java.util.logging.Logger;

import org.l2jdd.commons.network.BaseRecievePacket;
import org.l2jdd.loginserver.GameServerThread;

/**
 * @author -Wooden-
 */
public class PlayerLogout extends BaseRecievePacket
{
	protected static final Logger LOGGER = Logger.getLogger(PlayerLogout.class.getName());
	
	/**
	 * @param decrypt
	 * @param server
	 */
	public PlayerLogout(byte[] decrypt, GameServerThread server)
	{
		super(decrypt);
		final String account = readS();
		server.removeAccountOnGameServer(account);
	}
}
