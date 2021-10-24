
package org.l2jdd.gameserver.model.actor.instance;

import org.l2jdd.gameserver.enums.InstanceType;
import org.l2jdd.gameserver.enums.Team;
import org.l2jdd.gameserver.model.Party;
import org.l2jdd.gameserver.model.actor.Creature;
import org.l2jdd.gameserver.model.actor.Npc;
import org.l2jdd.gameserver.model.actor.templates.NpcTemplate;
import org.l2jdd.gameserver.network.serverpackets.ActionFailed;

public class EffectPointInstance extends Npc
{
	private final PlayerInstance _owner;
	
	public EffectPointInstance(NpcTemplate template, Creature owner)
	{
		super(template);
		setInstanceType(InstanceType.EffectPointInstance);
		setInvul(false);
		_owner = owner == null ? null : owner.getActingPlayer();
		if (owner != null)
		{
			setInstance(owner.getInstanceWorld());
		}
	}
	
	@Override
	public PlayerInstance getActingPlayer()
	{
		return _owner;
	}
	
	/**
	 * this is called when a player interacts with this NPC
	 * @param player
	 */
	@Override
	public void onAction(PlayerInstance player, boolean interact)
	{
		// Send a Server->Client ActionFailed to the PlayerInstance in order to avoid that the client wait another packet
		player.sendPacket(ActionFailed.STATIC_PACKET);
	}
	
	@Override
	public void onActionShift(PlayerInstance player)
	{
		if (player == null)
		{
			return;
		}
		
		player.sendPacket(ActionFailed.STATIC_PACKET);
	}
	
	/**
	 * Return the Party object of its PlayerInstance owner or null.
	 */
	@Override
	public Party getParty()
	{
		if (_owner == null)
		{
			return null;
		}
		return _owner.getParty();
	}
	
	/**
	 * Return True if the Creature has a Party in progress.
	 */
	@Override
	public boolean isInParty()
	{
		return (_owner != null) && _owner.isInParty();
	}
	
	@Override
	public int getClanId()
	{
		return (_owner != null) ? _owner.getClanId() : 0;
	}
	
	@Override
	public int getAllyId()
	{
		return (_owner != null) ? _owner.getAllyId() : 0;
	}
	
	@Override
	public byte getPvpFlag()
	{
		return _owner != null ? _owner.getPvpFlag() : 0;
	}
	
	@Override
	public Team getTeam()
	{
		return _owner != null ? _owner.getTeam() : Team.NONE;
	}
}