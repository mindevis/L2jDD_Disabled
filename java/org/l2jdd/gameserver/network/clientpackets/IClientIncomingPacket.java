
package org.l2jdd.gameserver.network.clientpackets;

import java.util.logging.Logger;

import org.l2jdd.commons.network.IIncomingPacket;
import org.l2jdd.gameserver.network.GameClient;

/**
 * Packets received by the game server from clients
 * @author KenM
 */
public interface IClientIncomingPacket extends IIncomingPacket<GameClient>
{
	Logger LOGGER = Logger.getLogger(IClientIncomingPacket.class.getName());
}
