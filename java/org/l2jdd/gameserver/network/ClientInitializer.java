
package org.l2jdd.gameserver.network;

import java.nio.ByteOrder;

import org.l2jdd.commons.network.codecs.CryptCodec;
import org.l2jdd.commons.network.codecs.LengthFieldBasedFrameEncoder;
import org.l2jdd.commons.network.codecs.PacketDecoder;
import org.l2jdd.commons.network.codecs.PacketEncoder;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

/**
 * @author Nos
 */
public class ClientInitializer extends ChannelInitializer<SocketChannel>
{
	private static final LengthFieldBasedFrameEncoder LENGTH_ENCODER = new LengthFieldBasedFrameEncoder();
	private static final PacketEncoder PACKET_ENCODER = new PacketEncoder(0x8000 - 2);
	
	@Override
	protected void initChannel(SocketChannel ch)
	{
		final GameClient client = new GameClient();
		ch.pipeline().addLast("length-decoder", new LengthFieldBasedFrameDecoder(ByteOrder.LITTLE_ENDIAN, 0x8000 - 2, 0, 2, -2, 2, false));
		ch.pipeline().addLast("length-encoder", LENGTH_ENCODER);
		ch.pipeline().addLast("crypt-codec", new CryptCodec(client.getCrypt()));
		// ch.pipeline().addLast(new LoggingHandler(LogLevel.INFO));
		ch.pipeline().addLast("packet-decoder", new PacketDecoder<>(IncomingPackets.PACKET_ARRAY, client));
		ch.pipeline().addLast("packet-encoder", PACKET_ENCODER);
		ch.pipeline().addLast(client);
	}
}
