
package handlers.bypasshandlers;

import org.l2jdd.gameserver.handler.IBypassHandler;
import org.l2jdd.gameserver.model.actor.Creature;
import org.l2jdd.gameserver.model.actor.Npc;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.network.serverpackets.equipmentupgrade.ExShowUpgradeSystem;

/**
 * @author Mobius
 */
public class UpgradeEquipment implements IBypassHandler
{
	private static final int FERRIS = 30847;
	
	private static final String[] COMMANDS =
	{
		"UpgradeEquipment"
	};
	
	@Override
	public boolean useBypass(String command, PlayerInstance player, Creature target)
	{
		if ((target == null) || !target.isNpc() || (((Npc) target).getId() != FERRIS))
		{
			return false;
		}
		player.sendPacket(new ExShowUpgradeSystem());
		return true;
	}
	
	@Override
	public String[] getBypassList()
	{
		return COMMANDS;
	}
}
