
package org.l2jdd.gameserver.network;

import org.l2jdd.commons.network.IConnectionState;

/**
 * @author KenM
 */
public enum ConnectionState implements IConnectionState
{
	CONNECTED,
	DISCONNECTED,
	CLOSING,
	AUTHENTICATED,
	ENTERING,
	IN_GAME;
}
