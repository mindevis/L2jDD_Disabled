
package handlers.admincommandhandlers;

import java.util.StringTokenizer;

import org.l2jdd.gameserver.data.xml.SkillData;
import org.l2jdd.gameserver.handler.IAdminCommandHandler;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.model.skills.Skill;

/**
 * The classical custom L2J implementation of the old //gmspeed GM command.
 * @author lord_rex (No, it wasn't me at all. Eclipse added my name there.)
 */
public class AdminSuperHaste implements IAdminCommandHandler
{
	static final String[] ADMIN_COMMANDS =
	{
		"admin_superhaste",
		"admin_superhaste_menu",
		"admin_speed",
		"admin_speed_menu",
	};
	
	private static final int SUPER_HASTE_ID = 7029;
	
	@Override
	public boolean useAdminCommand(String command, PlayerInstance player)
	{
		final StringTokenizer st = new StringTokenizer(command);
		final String cmd = st.nextToken();
		switch (cmd)
		{
			case "admin_superhaste":
			case "admin_speed":
			{
				try
				{
					final int val = Integer.parseInt(st.nextToken());
					final boolean sendMessage = player.isAffectedBySkill(SUPER_HASTE_ID);
					player.stopSkillEffects((val == 0) && sendMessage, SUPER_HASTE_ID);
					if ((val >= 1) && (val <= 4))
					{
						int time = 0;
						if (st.hasMoreTokens())
						{
							time = Integer.parseInt(st.nextToken());
						}
						
						final Skill superHasteSkill = SkillData.getInstance().getSkill(SUPER_HASTE_ID, val);
						superHasteSkill.applyEffects(player, player, true, time);
					}
				}
				catch (Exception e)
				{
					player.sendMessage("Usage: //superhaste <Effect level (0-4)> <Time in seconds>");
				}
				break;
			}
			case "admin_superhaste_menu":
			case "admin_speed_menu":
			{
				AdminHtml.showAdminHtml(player, "gm_menu.htm");
				break;
			}
		}
		
		return true;
	}
	
	@Override
	public String[] getAdminCommandList()
	{
		return ADMIN_COMMANDS;
	}
}
