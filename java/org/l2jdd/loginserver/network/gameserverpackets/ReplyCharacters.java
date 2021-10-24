
package org.l2jdd.loginserver.network.gameserverpackets;

import org.l2jdd.commons.network.BaseRecievePacket;
import org.l2jdd.loginserver.GameServerThread;
import org.l2jdd.loginserver.LoginController;

/**
 * Thanks to mochitto.
 * @author mrTJO
 */
public class ReplyCharacters extends BaseRecievePacket
{
	/**
	 * @param decrypt
	 * @param server
	 */
	public ReplyCharacters(byte[] decrypt, GameServerThread server)
	{
		super(decrypt);
		final String account = readS();
		final int chars = readC();
		final int charsToDel = readC();
		final long[] charsList = new long[charsToDel];
		for (int i = 0; i < charsToDel; i++)
		{
			charsList[i] = readQ();
		}
		LoginController.getInstance().setCharactersOnServer(account, chars, charsList, server.getServerId());
	}
}
