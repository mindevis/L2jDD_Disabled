
package handlers.effecthandlers;

import org.l2jdd.gameserver.data.xml.NpcData;
import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.actor.Creature;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.model.actor.instance.SiegeFlagInstance;
import org.l2jdd.gameserver.model.effects.AbstractEffect;
import org.l2jdd.gameserver.model.items.instance.ItemInstance;
import org.l2jdd.gameserver.model.skills.Skill;

/**
 * Headquarter Create effect implementation.
 * @author Adry_85
 */
public class HeadquarterCreate extends AbstractEffect
{
	private static final int HQ_NPC_ID = 35062;
	private final boolean _isAdvanced;
	
	public HeadquarterCreate(StatSet params)
	{
		_isAdvanced = params.getBoolean("isAdvanced", false);
	}
	
	@Override
	public boolean isInstant()
	{
		return true;
	}
	
	@Override
	public void instant(Creature effector, Creature effected, Skill skill, ItemInstance item)
	{
		final PlayerInstance player = effector.getActingPlayer();
		if ((player.getClan() == null) || (player.getClan().getLeaderId() != player.getObjectId()))
		{
			return;
		}
		
		final SiegeFlagInstance flag = new SiegeFlagInstance(player, NpcData.getInstance().getTemplate(HQ_NPC_ID), _isAdvanced);
		flag.setTitle(player.getClan().getName());
		flag.setCurrentHpMp(flag.getMaxHp(), flag.getMaxMp());
		flag.setHeading(player.getHeading());
		flag.spawnMe(player.getX(), player.getY(), player.getZ() + 50);
	}
}
