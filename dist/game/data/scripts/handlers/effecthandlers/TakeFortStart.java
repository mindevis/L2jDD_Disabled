
package handlers.effecthandlers;

import org.l2jdd.gameserver.instancemanager.FortManager;
import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.actor.Creature;
import org.l2jdd.gameserver.model.clan.Clan;
import org.l2jdd.gameserver.model.effects.AbstractEffect;
import org.l2jdd.gameserver.model.items.instance.ItemInstance;
import org.l2jdd.gameserver.model.siege.Fort;
import org.l2jdd.gameserver.model.skills.Skill;
import org.l2jdd.gameserver.network.SystemMessageId;
import org.l2jdd.gameserver.network.serverpackets.SystemMessage;

/**
 * Take Fort Start effect implementation.
 * @author UnAfraid
 */
public class TakeFortStart extends AbstractEffect
{
	public TakeFortStart(StatSet params)
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
		if (effector.isPlayer())
		{
			final Fort fort = FortManager.getInstance().getFort(effector);
			final Clan clan = effector.getClan();
			if ((fort != null) && (clan != null))
			{
				fort.getSiege().announceToPlayer(new SystemMessage(SystemMessageId.S1_CLAN_IS_TRYING_TO_DISPLAY_A_FLAG), clan.getName());
			}
		}
	}
}
