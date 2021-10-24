
package handlers.punishmenthandlers;

import org.l2jdd.gameserver.LoginServerThread;
import org.l2jdd.gameserver.handler.IPunishmentHandler;
import org.l2jdd.gameserver.model.World;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.model.punishment.PunishmentTask;
import org.l2jdd.gameserver.model.punishment.PunishmentType;
import org.l2jdd.gameserver.network.Disconnection;
import org.l2jdd.gameserver.network.GameClient;

/**
 * This class handles ban punishment.
 * @author UnAfraid
 */
public class BanHandler implements IPunishmentHandler
{
	@Override
	public void onStart(PunishmentTask task)
	{
		switch (task.getAffect())
		{
			case CHARACTER:
			{
				final int objectId = Integer.parseInt(String.valueOf(task.getKey()));
				final PlayerInstance player = World.getInstance().getPlayer(objectId);
				if (player != null)
				{
					applyToPlayer(player);
				}
				break;
			}
			case ACCOUNT:
			{
				final String account = String.valueOf(task.getKey());
				final GameClient client = LoginServerThread.getInstance().getClient(account);
				if (client != null)
				{
					final PlayerInstance player = client.getPlayer();
					if (player != null)
					{
						applyToPlayer(player);
					}
					else
					{
						Disconnection.of(client).defaultSequence(false);
					}
				}
				break;
			}
			case IP:
			{
				final String ip = String.valueOf(task.getKey());
				for (PlayerInstance player : World.getInstance().getPlayers())
				{
					if (player.getIPAddress().equals(ip))
					{
						applyToPlayer(player);
					}
				}
				break;
			}
		}
	}
	
	@Override
	public void onEnd(PunishmentTask task)
	{
		// Should not do anything.
	}
	
	/**
	 * Applies all punishment effects from the player.
	 * @param player
	 */
	private void applyToPlayer(PlayerInstance player)
	{
		Disconnection.of(player).defaultSequence(false);
	}
	
	@Override
	public PunishmentType getType()
	{
		return PunishmentType.BAN;
	}
}
