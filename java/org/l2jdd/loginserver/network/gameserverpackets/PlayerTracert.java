
package org.l2jdd.loginserver.network.gameserverpackets;

import java.util.logging.Logger;

import org.l2jdd.commons.network.BaseRecievePacket;
import org.l2jdd.loginserver.LoginController;

/**
 * @author mrTJO
 */
public class PlayerTracert extends BaseRecievePacket
{
	protected static final Logger LOGGER = Logger.getLogger(PlayerTracert.class.getName());
	
	/**
	 * @param decrypt
	 */
	public PlayerTracert(byte[] decrypt)
	{
		super(decrypt);
		final String account = readS();
		final String pcIp = readS();
		final String hop1 = readS();
		final String hop2 = readS();
		final String hop3 = readS();
		final String hop4 = readS();
		LoginController.getInstance().setAccountLastTracert(account, pcIp, hop1, hop2, hop3, hop4);
	}
}
