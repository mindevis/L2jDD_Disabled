
package handlers.effecthandlers;

import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.actor.Creature;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.model.effects.AbstractEffect;
import org.l2jdd.gameserver.model.items.instance.ItemInstance;
import org.l2jdd.gameserver.model.skills.Skill;
import org.l2jdd.gameserver.model.stats.Stat;
import org.l2jdd.gameserver.network.SystemMessageId;
import org.l2jdd.gameserver.network.serverpackets.EtcStatusUpdate;
import org.l2jdd.gameserver.network.serverpackets.SystemMessage;

/**
 * Focus Max Energy effect implementation.
 * @author Adry_85
 */
public class FocusMaxMomentum extends AbstractEffect
{
	public FocusMaxMomentum(StatSet params)
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
		if (effected.isPlayer())
		{
			final PlayerInstance player = effected.getActingPlayer();
			
			final int count = (int) effected.getStat().getValue(Stat.MAX_MOMENTUM, 1);
			
			player.setCharges(count);
			
			final SystemMessage sm = new SystemMessage(SystemMessageId.YOUR_FORCE_HAS_INCREASED_TO_LEVEL_S1);
			sm.addInt(count);
			player.sendPacket(sm);
			
			player.sendPacket(new EtcStatusUpdate(player));
		}
	}
}