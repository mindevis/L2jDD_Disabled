
package handlers.itemhandlers;

import org.l2jdd.commons.util.Rnd;
import org.l2jdd.gameserver.geoengine.GeoEngine;
import org.l2jdd.gameserver.handler.IItemHandler;
import org.l2jdd.gameserver.model.Location;
import org.l2jdd.gameserver.model.actor.Playable;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.model.items.instance.ItemInstance;
import org.l2jdd.gameserver.model.zone.ZoneId;
import org.l2jdd.gameserver.network.SystemMessageId;
import org.l2jdd.gameserver.network.serverpackets.Dice;
import org.l2jdd.gameserver.network.serverpackets.SystemMessage;
import org.l2jdd.gameserver.util.Broadcast;
import org.l2jdd.gameserver.util.Util;

public class RollingDice implements IItemHandler
{
	@Override
	public boolean useItem(Playable playable, ItemInstance item, boolean forceUse)
	{
		if (!playable.isPlayer())
		{
			playable.sendPacket(SystemMessageId.YOUR_PET_CANNOT_CARRY_THIS_ITEM);
			return false;
		}
		
		final PlayerInstance player = playable.getActingPlayer();
		final int itemId = item.getId();
		if (player.isInOlympiadMode())
		{
			player.sendPacket(SystemMessageId.YOU_CANNOT_USE_THAT_ITEM_IN_A_OLYMPIAD_MATCH);
			return false;
		}
		
		final int number = rollDice(player);
		if (number == 0)
		{
			player.sendPacket(SystemMessageId.YOU_MAY_NOT_THROW_THE_DICE_AT_THIS_TIME_TRY_AGAIN_LATER);
			return false;
		}
		
		// Mobius: Retail dice position land calculation.
		final double angle = Util.convertHeadingToDegree(player.getHeading());
		final double radian = Math.toRadians(angle);
		final double course = Math.toRadians(180);
		final int x1 = (int) (Math.cos(Math.PI + radian + course) * 40);
		final int y1 = (int) (Math.sin(Math.PI + radian + course) * 40);
		final int x = player.getX() + x1;
		final int y = player.getY() + y1;
		final int z = player.getZ();
		final Location destination = GeoEngine.getInstance().getValidLocation(player.getX(), player.getY(), player.getZ(), x, y, z, player.getInstanceWorld());
		Broadcast.toSelfAndKnownPlayers(player, new Dice(player.getObjectId(), itemId, number, destination.getX(), destination.getY(), destination.getZ()));
		
		final SystemMessage sm = new SystemMessage(SystemMessageId.C1_HAS_ROLLED_A_S2);
		sm.addString(player.getName());
		sm.addInt(number);
		
		player.sendPacket(sm);
		if (player.isInsideZone(ZoneId.PEACE))
		{
			Broadcast.toKnownPlayers(player, sm);
		}
		else if (player.isInParty()) // TODO: Verify this!
		{
			player.getParty().broadcastToPartyMembers(player, sm);
		}
		return true;
	}
	
	/**
	 * @param player
	 * @return
	 */
	private int rollDice(PlayerInstance player)
	{
		// Check if the dice is ready
		if (!player.getFloodProtectors().getRollDice().tryPerformAction("roll dice"))
		{
			return 0;
		}
		return Rnd.get(1, 6);
	}
}
