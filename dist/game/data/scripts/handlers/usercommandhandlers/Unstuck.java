
package handlers.usercommandhandlers;

import static org.l2jdd.gameserver.ai.CtrlIntention.AI_INTENTION_ACTIVE;

import org.l2jdd.Config;
import org.l2jdd.gameserver.data.xml.SkillData;
import org.l2jdd.gameserver.handler.IUserCommandHandler;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.model.skills.Skill;
import org.l2jdd.gameserver.model.skills.SkillCaster;
import org.l2jdd.gameserver.model.skills.SkillCastingType;
import org.l2jdd.gameserver.network.SystemMessageId;
import org.l2jdd.gameserver.network.serverpackets.ActionFailed;

/**
 * Unstuck user command.
 */
public class Unstuck implements IUserCommandHandler
{
	private static final int[] COMMAND_IDS =
	{
		52
	};
	
	@Override
	public boolean useUserCommand(int id, PlayerInstance player)
	{
		if (player.isJailed())
		{
			player.sendMessage("You cannot use this function while you are jailed.");
			return false;
		}
		
		if (Config.FACTION_SYSTEM_ENABLED && !player.isGood() && !player.isEvil())
		{
			player.sendMessage("You cannot use this function while you are neutral.");
			return false;
		}
		
		final int unstuckTimer = (player.getAccessLevel().isGm() ? 1000 : Config.UNSTUCK_INTERVAL * 1000);
		
		if (player.isInOlympiadMode())
		{
			player.sendPacket(SystemMessageId.YOU_CANNOT_USE_THAT_SKILL_IN_A_OLYMPIAD_MATCH);
			return false;
		}
		
		if (player.isCastingNow(SkillCaster::isAnyNormalType) || player.isMovementDisabled() || player.isMuted() || player.isAlikeDead() || player.inObserverMode() || player.isCombatFlagEquipped())
		{
			return false;
		}
		
		final Skill escape = SkillData.getInstance().getSkill(2099, 1); // 5 minutes escape
		final Skill gmEscape = SkillData.getInstance().getSkill(2100, 1); // 1 second escape
		if (player.getAccessLevel().isGm())
		{
			if (gmEscape != null)
			{
				player.doCast(gmEscape);
				return true;
			}
			player.sendMessage("You use Escape: 1 second.");
		}
		else if ((Config.UNSTUCK_INTERVAL == 300) && (escape != null))
		{
			player.doCast(escape);
			return true;
		}
		else
		{
			final SkillCaster skillCaster = SkillCaster.castSkill(player, player.getTarget(), escape, null, SkillCastingType.NORMAL, false, false, unstuckTimer);
			if (skillCaster == null)
			{
				player.sendPacket(ActionFailed.get(SkillCastingType.NORMAL));
				player.getAI().setIntention(AI_INTENTION_ACTIVE);
				return false;
			}
			
			if (Config.UNSTUCK_INTERVAL > 100)
			{
				player.sendMessage("You use Escape: " + (unstuckTimer / 60000) + " minutes.");
			}
			else
			{
				player.sendMessage("You use Escape: " + (unstuckTimer / 1000) + " seconds.");
			}
		}
		return true;
	}
	
	@Override
	public int[] getUserCommandList()
	{
		return COMMAND_IDS;
	}
}
