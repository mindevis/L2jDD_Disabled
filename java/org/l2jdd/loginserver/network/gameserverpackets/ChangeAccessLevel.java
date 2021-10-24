
package org.l2jdd.loginserver.network.gameserverpackets;

import java.util.logging.Logger;

import org.l2jdd.commons.network.BaseRecievePacket;
import org.l2jdd.loginserver.GameServerThread;
import org.l2jdd.loginserver.LoginController;

/**
 * @author -Wooden-
 */
public class ChangeAccessLevel extends BaseRecievePacket
{
	protected static final Logger LOGGER = Logger.getLogger(ChangeAccessLevel.class.getName());
	
	/**
	 * @param decrypt
	 * @param server
	 */
	public ChangeAccessLevel(byte[] decrypt, GameServerThread server)
	{
		super(decrypt);
		final int level = readD();
		final String account = readS();
		LoginController.getInstance().setAccountAccessLevel(account, level);
		LOGGER.info("Changed " + account + " access level to " + level);
	}
}
