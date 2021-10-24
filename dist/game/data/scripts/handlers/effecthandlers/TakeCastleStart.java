
package handlers.effecthandlers;

import org.l2jdd.gameserver.instancemanager.CastleManager;
import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.actor.Creature;
import org.l2jdd.gameserver.model.effects.AbstractEffect;
import org.l2jdd.gameserver.model.items.instance.ItemInstance;
import org.l2jdd.gameserver.model.siege.Castle;
import org.l2jdd.gameserver.model.skills.Skill;
import org.l2jdd.gameserver.network.SystemMessageId;
import org.l2jdd.gameserver.network.serverpackets.SystemMessage;

/**
 * Take Castle Start effect implementation.
 * @author St3eT
 */
public class TakeCastleStart extends AbstractEffect
{
	public TakeCastleStart(StatSet params)
	{
	}
	
	@Override
	public boolean isInstant()
	{
		return true;
	}
	
	@Override
	public void instant(Creature effector, Creature effected, Skill skill, ItemInstance item)
	{
		if (!effector.isPlayer())
		{
			return;
		}
		
		final Castle castle = CastleManager.getInstance().getCastle(effected);
		if ((castle != null) && castle.getSiege().isInProgress())
		{
			castle.getSiege().announceToPlayer(new SystemMessage(SystemMessageId.THE_OPPOSING_CLAN_HAS_STARTED_S1).addSkillName(skill.getId()), false);
		}
	}
}