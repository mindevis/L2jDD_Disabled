
package org.l2jdd.commons.network;

import io.netty.buffer.ByteBuf;

/**
 * @author Nos
 */
public interface ICrypt
{
	void encrypt(ByteBuf buf);
	
	void decrypt(ByteBuf buf);
}
