
package handlers.effecthandlers;

import org.l2jdd.gameserver.ai.CtrlIntention;
import org.l2jdd.gameserver.model.Party;
import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.actor.Creature;
import org.l2jdd.gameserver.model.actor.Npc;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.model.effects.AbstractEffect;
import org.l2jdd.gameserver.model.effects.EffectType;
import org.l2jdd.gameserver.model.interfaces.ILocational;
import org.l2jdd.gameserver.model.items.instance.ItemInstance;
import org.l2jdd.gameserver.model.skills.Skill;
import org.l2jdd.gameserver.network.serverpackets.FlyToLocation;
import org.l2jdd.gameserver.network.serverpackets.FlyToLocation.FlyType;
import org.l2jdd.gameserver.network.serverpackets.ValidateLocation;

/**
 * Teleport player/party to summoned npc effect implementation.
 * @author Nik
 */
public class TeleportToNpc extends AbstractEffect
{
	private final int _npcId;
	private final boolean _party;
	
	public TeleportToNpc(StatSet params)
	{
		_npcId = params.getInt("npcId");
		_party = params.getBoolean("party", false);
	}
	
	@Override
	public EffectType getEffectType()
	{
		return EffectType.TELEPORT_TO_TARGET;
	}
	
	@Override
	public boolean isInstant()
	{
		return true;
	}
	
	@Override
	public void instant(Creature effector, Creature effected, Skill skill, ItemInstance item)
	{
		ILocational teleLocation = null;
		for (Npc npc : effector.getSummonedNpcs())
		{
			if (npc.getId() == _npcId)
			{
				teleLocation = npc;
			}
		}
		
		if (teleLocation != null)
		{
			final Party party = effected.getParty();
			if (_party && (party != null))
			{
				for (PlayerInstance member : party.getMembers())
				{
					teleport(member, teleLocation);
				}
			}
			else
			{
				teleport(effected, teleLocation);
			}
		}
	}
	
	private void teleport(Creature effected, ILocational location)
	{
		if (effected.isInsideRadius2D(location, 900))
		{
			effected.getAI().setIntention(CtrlIntention.AI_INTENTION_IDLE);
			effected.broadcastPacket(new FlyToLocation(effected, location, FlyType.DUMMY));
			effected.abortAttack();
			effected.abortCast();
			effected.setXYZ(location);
			effected.broadcastPacket(new ValidateLocation(effected));
			effected.revalidateZone(true);
		}
		else
		{
			effected.teleToLocation(location);
		}
	}
}
