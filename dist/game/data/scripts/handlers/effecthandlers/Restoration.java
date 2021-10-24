
package handlers.effecthandlers;

import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.actor.Creature;
import org.l2jdd.gameserver.model.effects.AbstractEffect;
import org.l2jdd.gameserver.model.effects.EffectType;
import org.l2jdd.gameserver.model.items.instance.ItemInstance;
import org.l2jdd.gameserver.model.skills.Skill;
import org.l2jdd.gameserver.network.SystemMessageId;
import org.l2jdd.gameserver.network.serverpackets.PetItemList;

/**
 * Restoration effect implementation.
 * @author Zoey76, Mobius
 */
public class Restoration extends AbstractEffect
{
	private final int _itemId;
	private final int _itemCount;
	private final int _itemEnchantmentLevel;
	
	public Restoration(StatSet params)
	{
		_itemId = params.getInt("itemId", 0);
		_itemCount = params.getInt("itemCount", 0);
		_itemEnchantmentLevel = params.getInt("itemEnchantmentLevel", 0);
	}
	
	@Override
	public boolean isInstant()
	{
		return true;
	}
	
	@Override
	public void instant(Creature effector, Creature effected, Skill skill, ItemInstance item)
	{
		if (!effected.isPlayable())
		{
			return;
		}
		
		if ((_itemId <= 0) || (_itemCount <= 0))
		{
			effected.sendPacket(SystemMessageId.THERE_WAS_NOTHING_FOUND_INSIDE);
			LOGGER.warning(Restoration.class.getSimpleName() + " effect with wrong item Id/count: " + _itemId + "/" + _itemCount + "!");
			return;
		}
		
		if (effected.isPlayer())
		{
			final ItemInstance newItem = effected.getActingPlayer().addItem("Skill", _itemId, _itemCount, effector, true);
			if (_itemEnchantmentLevel > 0)
			{
				newItem.setEnchantLevel(_itemEnchantmentLevel);
			}
		}
		else if (effected.isPet())
		{
			final ItemInstance newItem = effected.getInventory().addItem("Skill", _itemId, _itemCount, effected.getActingPlayer(), effector);
			if (_itemEnchantmentLevel > 0)
			{
				newItem.setEnchantLevel(_itemEnchantmentLevel);
			}
			effected.getActingPlayer().sendPacket(new PetItemList(effected.getInventory().getItems()));
		}
	}
	
	@Override
	public EffectType getEffectType()
	{
		return EffectType.EXTRACT_ITEM;
	}
}
