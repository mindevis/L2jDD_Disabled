
package org.l2jdd.commons.network;

import java.util.Set;

/**
 * @author Nos
 * @param <T>
 */
public interface IIncomingPackets<T>extends IConnectionState
{
	int getPacketId();
	
	IIncomingPacket<T> newIncomingPacket();
	
	Set<IConnectionState> getConnectionStates();
}
