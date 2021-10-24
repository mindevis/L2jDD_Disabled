
package org.l2jdd.gameserver.network.clientpackets;

import java.util.Calendar;
import java.util.Date;

import org.l2jdd.Config;
import org.l2jdd.commons.network.PacketReader;
import org.l2jdd.gameserver.instancemanager.CastleManager;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.model.siege.Castle;
import org.l2jdd.gameserver.network.GameClient;
import org.l2jdd.gameserver.network.SystemMessageId;
import org.l2jdd.gameserver.network.serverpackets.SiegeInfo;
import org.l2jdd.gameserver.network.serverpackets.SystemMessage;
import org.l2jdd.gameserver.util.Broadcast;

/**
 * @author UnAfraid
 */
public class RequestSetCastleSiegeTime implements IClientIncomingPacket
{
	private int _castleId;
	private long _time;
	
	@Override
	public boolean read(GameClient client, PacketReader packet)
	{
		_castleId = packet.readD();
		_time = packet.readD();
		_time *= 1000;
		return true;
	}
	
	@Override
	public void run(GameClient client)
	{
		final PlayerInstance player = client.getPlayer();
		final Castle castle = CastleManager.getInstance().getCastleById(_castleId);
		if ((player == null) || (castle == null))
		{
			LOGGER.warning(getClass().getSimpleName() + ": activeChar: " + player + " castle: " + castle + " castleId: " + _castleId);
			return;
		}
		if ((castle.getOwnerId() > 0) && (castle.getOwnerId() != player.getClanId()))
		{
			LOGGER.warning(getClass().getSimpleName() + ": activeChar: " + player + " castle: " + castle + " castleId: " + _castleId + " is trying to change siege date of not his own castle!");
		}
		else if (!player.isClanLeader())
		{
			LOGGER.warning(getClass().getSimpleName() + ": activeChar: " + player + " castle: " + castle + " castleId: " + _castleId + " is trying to change siege date but is not clan leader!");
		}
		else if (!castle.isTimeRegistrationOver())
		{
			if (isSiegeTimeValid(castle.getSiegeDate().getTimeInMillis(), _time))
			{
				castle.getSiegeDate().setTimeInMillis(_time);
				castle.setTimeRegistrationOver(true);
				castle.getSiege().saveSiegeDate();
				final SystemMessage msg = new SystemMessage(SystemMessageId.S1_HAS_ANNOUNCED_THE_NEXT_CASTLE_SIEGE_TIME);
				msg.addCastleId(_castleId);
				Broadcast.toAllOnlinePlayers(msg);
				player.sendPacket(new SiegeInfo(castle, player));
			}
			else
			{
				LOGGER.warning(getClass().getSimpleName() + ": activeChar: " + player + " castle: " + castle + " castleId: " + _castleId + " is trying to an invalid time (" + new Date(_time) + " !");
			}
		}
		else
		{
			LOGGER.warning(getClass().getSimpleName() + ": activeChar: " + player + " castle: " + castle + " castleId: " + _castleId + " is trying to change siege date but currently not possible!");
		}
	}
	
	private static boolean isSiegeTimeValid(long siegeDate, long choosenDate)
	{
		final Calendar cal1 = Calendar.getInstance();
		cal1.setTimeInMillis(siegeDate);
		cal1.set(Calendar.MINUTE, 0);
		cal1.set(Calendar.SECOND, 0);
		
		final Calendar cal2 = Calendar.getInstance();
		cal2.setTimeInMillis(choosenDate);
		
		for (int hour : Config.SIEGE_HOUR_LIST)
		{
			cal1.set(Calendar.HOUR_OF_DAY, hour);
			if (isEqual(cal1, cal2, Calendar.YEAR, Calendar.MONTH, Calendar.DAY_OF_MONTH, Calendar.HOUR, Calendar.MINUTE, Calendar.SECOND))
			{
				return true;
			}
		}
		return false;
	}
	
	private static boolean isEqual(Calendar cal1, Calendar cal2, int... fields)
	{
		for (int field : fields)
		{
			if (cal1.get(field) != cal2.get(field))
			{
				return false;
			}
		}
		return true;
	}
}
