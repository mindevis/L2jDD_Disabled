
package handlers.effecthandlers;

import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.stats.Stat;

/**
 * @author Nik
 */
public class WorldChatPoints extends AbstractStatEffect
{
	public WorldChatPoints(StatSet params)
	{
		super(params, Stat.WORLD_CHAT_POINTS);
	}
}
