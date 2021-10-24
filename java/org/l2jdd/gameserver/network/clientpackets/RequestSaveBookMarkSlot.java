
package org.l2jdd.gameserver.network.clientpackets;

import org.l2jdd.commons.network.PacketReader;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.model.zone.ZoneId;
import org.l2jdd.gameserver.network.GameClient;

/**
 * @author ShanSoft
 * @structure chdSdS
 */
public class RequestSaveBookMarkSlot implements IClientIncomingPacket
{
	private int icon;
	private String name;
	private String tag;
	
	@Override
	public boolean read(GameClient client, PacketReader packet)
	{
		name = packet.readS();
		icon = packet.readD();
		tag = packet.readS();
		return true;
	}
	
	@Override
	public void run(GameClient client)
	{
		final PlayerInstance player = client.getPlayer();
		if (player == null)
		{
			return;
		}
		
		if (player.isInsideZone(ZoneId.TIMED_HUNTING))
		{
			player.sendMessage("You cannot bookmark this location.");
			return;
		}
		
		player.teleportBookmarkAdd(player.getX(), player.getY(), player.getZ(), icon, tag, name);
	}
}
