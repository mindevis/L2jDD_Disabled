
package handlers.effecthandlers;

import org.l2jdd.gameserver.enums.StorageType;
import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.actor.Creature;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.model.effects.AbstractEffect;
import org.l2jdd.gameserver.model.skills.Skill;
import org.l2jdd.gameserver.model.stats.Stat;
import org.l2jdd.gameserver.network.serverpackets.ExStorageMaxCount;

/**
 * @author Sdw
 */
public class EnlargeSlot extends AbstractEffect
{
	private final StorageType _type;
	private final double _amount;
	
	public EnlargeSlot(StatSet params)
	{
		_amount = params.getDouble("amount", 0);
		_type = params.getEnum("type", StorageType.class, StorageType.INVENTORY_NORMAL);
	}
	
	@Override
	public void pump(Creature effected, Skill skill)
	{
		Stat stat = Stat.INVENTORY_NORMAL;
		
		switch (_type)
		{
			case TRADE_BUY:
			{
				stat = Stat.TRADE_BUY;
				break;
			}
			case TRADE_SELL:
			{
				stat = Stat.TRADE_SELL;
				break;
			}
			case RECIPE_DWARVEN:
			{
				stat = Stat.RECIPE_DWARVEN;
				break;
			}
			case RECIPE_COMMON:
			{
				stat = Stat.RECIPE_COMMON;
				break;
			}
			case STORAGE_PRIVATE:
			{
				stat = Stat.STORAGE_PRIVATE;
				break;
			}
		}
		effected.getStat().mergeAdd(stat, _amount);
		if (effected.isPlayer())
		{
			effected.sendPacket(new ExStorageMaxCount((PlayerInstance) effected));
		}
	}
}
