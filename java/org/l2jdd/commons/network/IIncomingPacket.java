
package org.l2jdd.commons.network;

/**
 * @author Nos
 * @param <T>
 */
public interface IIncomingPacket<T>
{
	/**
	 * Reads a packet.
	 * @param client the client
	 * @param packet the packet reader
	 * @return {@code true} if packet was read successfully, {@code false} otherwise.
	 */
	boolean read(T client, PacketReader packet);
	
	void run(T client) throws Exception;
}
