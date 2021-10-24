
package handlers.actionhandlers;

import org.l2jdd.gameserver.ai.CtrlIntention;
import org.l2jdd.gameserver.data.xml.ClanHallData;
import org.l2jdd.gameserver.enums.InstanceType;
import org.l2jdd.gameserver.handler.IActionHandler;
import org.l2jdd.gameserver.model.WorldObject;
import org.l2jdd.gameserver.model.actor.Creature;
import org.l2jdd.gameserver.model.actor.Npc;
import org.l2jdd.gameserver.model.actor.instance.DoorInstance;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.model.holders.DoorRequestHolder;
import org.l2jdd.gameserver.model.residences.ClanHall;
import org.l2jdd.gameserver.network.serverpackets.ConfirmDlg;

public class DoorInstanceAction implements IActionHandler
{
	@Override
	public boolean action(PlayerInstance player, WorldObject target, boolean interact)
	{
		// Check if the PlayerInstance already target the NpcInstance
		if (player.getTarget() != target)
		{
			player.setTarget(target);
		}
		else if (interact)
		{
			final DoorInstance door = (DoorInstance) target;
			final ClanHall clanHall = ClanHallData.getInstance().getClanHallByDoorId(door.getId());
			// MyTargetSelected my = new MyTargetSelected(getObjectId(), player.getLevel());
			// player.sendPacket(my);
			if (target.isAutoAttackable(player))
			{
				if (Math.abs(player.getZ() - target.getZ()) < 400)
				{
					player.getAI().setIntention(CtrlIntention.AI_INTENTION_ATTACK, target);
				}
			}
			else if ((player.getClan() != null) && (clanHall != null) && (player.getClanId() == clanHall.getOwnerId()))
			{
				if (!door.isInsideRadius2D(player, Npc.INTERACTION_DISTANCE))
				{
					player.getAI().setIntention(CtrlIntention.AI_INTENTION_INTERACT, target);
				}
				else
				{
					player.addScript(new DoorRequestHolder(door));
					if (!door.isOpen())
					{
						player.sendPacket(new ConfirmDlg(1140));
					}
					else
					{
						player.sendPacket(new ConfirmDlg(1141));
					}
				}
			}
			else if ((player.getClan() != null) && (((DoorInstance) target).getFort() != null) && (player.getClan() == ((DoorInstance) target).getFort().getOwnerClan()) && ((DoorInstance) target).isOpenableBySkill() && !((DoorInstance) target).getFort().getSiege().isInProgress())
			{
				if (!((Creature) target).isInsideRadius2D(player, Npc.INTERACTION_DISTANCE))
				{
					player.getAI().setIntention(CtrlIntention.AI_INTENTION_INTERACT, target);
				}
				else
				{
					player.addScript(new DoorRequestHolder((DoorInstance) target));
					if (!((DoorInstance) target).isOpen())
					{
						player.sendPacket(new ConfirmDlg(1140));
					}
					else
					{
						player.sendPacket(new ConfirmDlg(1141));
					}
				}
			}
		}
		return true;
	}
	
	@Override
	public InstanceType getInstanceType()
	{
		return InstanceType.DoorInstance;
	}
}
