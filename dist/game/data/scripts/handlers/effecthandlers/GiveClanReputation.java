
package handlers.effecthandlers;

import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.actor.Creature;
import org.l2jdd.gameserver.model.clan.ClanMember;
import org.l2jdd.gameserver.model.effects.AbstractEffect;
import org.l2jdd.gameserver.model.items.instance.ItemInstance;
import org.l2jdd.gameserver.model.skills.Skill;
import org.l2jdd.gameserver.network.SystemMessageId;
import org.l2jdd.gameserver.network.serverpackets.SystemMessage;

/**
 * Give Clan reputation effect implementation.
 * @author Mobius
 */
public class GiveClanReputation extends AbstractEffect
{
	private final int _reputation;
	
	public GiveClanReputation(StatSet params)
	{
		_reputation = params.getInt("reputation", 0);
	}
	
	@Override
	public boolean isInstant()
	{
		return true;
	}
	
	@Override
	public void instant(Creature effector, Creature effected, Skill skill, ItemInstance item)
	{
		if (!effector.isPlayer() || !effected.isPlayer() || effected.isAlikeDead() || (effector.getActingPlayer().getClan() == null))
		{
			return;
		}
		
		effector.getActingPlayer().getClan().addReputationScore(_reputation, true);
		
		for (ClanMember member : effector.getActingPlayer().getClan().getMembers())
		{
			if (member.isOnline())
			{
				final SystemMessage sm = new SystemMessage(SystemMessageId.YOUR_CLAN_HAS_ADDED_S1_POINT_S_TO_ITS_CLAN_REPUTATION);
				sm.addInt(_reputation);
				member.getPlayerInstance().sendPacket(sm);
			}
		}
	}
}