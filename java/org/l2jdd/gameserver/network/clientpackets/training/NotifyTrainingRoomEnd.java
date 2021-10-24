
package org.l2jdd.gameserver.network.clientpackets.training;

import org.l2jdd.commons.network.PacketReader;
import org.l2jdd.commons.util.Chronos;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.model.holders.TrainingHolder;
import org.l2jdd.gameserver.network.GameClient;
import org.l2jdd.gameserver.network.clientpackets.IClientIncomingPacket;
import org.l2jdd.gameserver.network.serverpackets.training.ExTrainingZone_Leaving;

/**
 * @author Sdw
 */
public class NotifyTrainingRoomEnd implements IClientIncomingPacket
{
	@Override
	public boolean read(GameClient client, PacketReader packet)
	{
		// Nothing to read
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
		
		final TrainingHolder holder = player.getTraingCampInfo();
		if (holder == null)
		{
			return;
		}
		
		if (holder.isTraining())
		{
			holder.setEndTime(Chronos.currentTimeMillis());
			player.setTraingCampInfo(holder);
			player.enableAllSkills();
			player.setInvul(false);
			player.setInvisible(false);
			player.setImmobilized(false);
			player.teleToLocation(player.getLastLocation());
			player.sendPacket(ExTrainingZone_Leaving.STATIC_PACKET);
			holder.setEndTime(Chronos.currentTimeMillis());
			player.setTraingCampInfo(holder);
		}
	}
}
