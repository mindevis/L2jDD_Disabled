
package org.l2jdd.gameserver.network.serverpackets;

import org.l2jdd.commons.network.PacketWriter;
import org.l2jdd.gameserver.network.OutgoingPackets;

/**
 * @author devScarlet, mrTJO
 */
public class ExPlayScene implements IClientOutgoingPacket
{
	public static final ExPlayScene STATIC_PACKET = new ExPlayScene();
	
	private ExPlayScene()
	{
	}
	
	@Override
	public boolean write(PacketWriter packet)
	{
		OutgoingPackets.EX_PLAY_SCENE.writeId(packet);
		
		return true;
	}
}
