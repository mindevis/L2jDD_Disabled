
package handlers.admincommandhandlers;

import java.util.EnumSet;
import java.util.Set;
import java.util.StringTokenizer;

import org.l2jdd.Config;
import org.l2jdd.gameserver.handler.AdminCommandHandler;
import org.l2jdd.gameserver.handler.IAdminCommandHandler;
import org.l2jdd.gameserver.model.WorldObject;
import org.l2jdd.gameserver.model.actor.Creature;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.model.stats.Stat;
import org.l2jdd.gameserver.util.BuilderUtil;
import org.l2jdd.gameserver.util.Util;

/**
 * A retail-like implementation of //gmspeed builder command.
 * @author lord_rex
 */
public class AdminGmSpeed implements IAdminCommandHandler
{
	private static final String[] ADMIN_COMMANDS =
	{
		"admin_gmspeed",
	};
	
	private static final Set<Stat> SPEED_STATS = EnumSet.of(Stat.RUN_SPEED, Stat.WALK_SPEED, Stat.SWIM_RUN_SPEED, Stat.SWIM_WALK_SPEED, Stat.FLY_RUN_SPEED, Stat.FLY_WALK_SPEED);
	
	@Override
	public boolean useAdminCommand(String command, PlayerInstance player)
	{
		final StringTokenizer st = new StringTokenizer(command);
		final String cmd = st.nextToken();
		if (cmd.equals("admin_gmspeed"))
		{
			if (!st.hasMoreTokens())
			{
				BuilderUtil.sendSysMessage(player, "//gmspeed [0...10]");
				return false;
			}
			final String token = st.nextToken();
			
			// Rollback feature for old custom way, in order to make everyone happy.
			if (Config.USE_SUPER_HASTE_AS_GM_SPEED)
			{
				AdminCommandHandler.getInstance().useAdminCommand(player, AdminSuperHaste.ADMIN_COMMANDS[0] + " " + token, false);
				return true;
			}
			
			if (!Util.isDouble(token))
			{
				BuilderUtil.sendSysMessage(player, "//gmspeed [0...10]");
				return false;
			}
			final double runSpeedBoost = Double.parseDouble(token);
			if ((runSpeedBoost < 0) || (runSpeedBoost > 10))
			{
				// Custom limit according to SDW's request - real retail limit is unknown.
				BuilderUtil.sendSysMessage(player, "//gmspeed [0...10]");
				return false;
			}
			
			final Creature targetCharacter;
			final WorldObject target = player.getTarget();
			if ((target != null) && target.isCreature())
			{
				targetCharacter = (Creature) target;
			}
			else
			{
				// If there is no target, let's use the command executer.
				targetCharacter = player;
			}
			
			SPEED_STATS.forEach(speedStat -> targetCharacter.getStat().removeFixedValue(speedStat));
			if (runSpeedBoost > 0)
			{
				SPEED_STATS.forEach(speedStat -> targetCharacter.getStat().addFixedValue(speedStat, targetCharacter.getTemplate().getBaseValue(speedStat, 120) * runSpeedBoost));
			}
			
			targetCharacter.getStat().recalculateStats(false);
			if (targetCharacter.isPlayer())
			{
				((PlayerInstance) targetCharacter).broadcastUserInfo();
			}
			else
			{
				targetCharacter.broadcastInfo();
			}
			
			BuilderUtil.sendSysMessage(player, "[" + targetCharacter.getName() + "] speed is [" + (runSpeedBoost * 100) + "0]% fast.");
		}
		return true;
	}
	
	@Override
	public String[] getAdminCommandList()
	{
		return ADMIN_COMMANDS;
	}
}
