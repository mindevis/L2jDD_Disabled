
package handlers.itemhandlers;

import org.l2jdd.gameserver.data.xml.PetDataTable;
import org.l2jdd.gameserver.model.PetData;
import org.l2jdd.gameserver.model.actor.Playable;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.model.holders.PetItemHolder;
import org.l2jdd.gameserver.model.items.instance.ItemInstance;
import org.l2jdd.gameserver.network.SystemMessageId;

/**
 * @author HorridoJoho, UnAfraid
 */
public class SummonItems extends ItemSkillsTemplate
{
	@Override
	public boolean useItem(Playable playable, ItemInstance item, boolean forceUse)
	{
		if (!playable.isPlayer())
		{
			playable.sendPacket(SystemMessageId.YOUR_PET_CANNOT_CARRY_THIS_ITEM);
			return false;
		}
		
		final PlayerInstance player = playable.getActingPlayer();
		if (!player.getFloodProtectors().getItemPetSummon().tryPerformAction("summon items") || (player.getBlockCheckerArena() != -1) || player.inObserverMode() || player.isAllSkillsDisabled() || player.isCastingNow())
		{
			return false;
		}
		
		if (player.isSitting())
		{
			player.sendPacket(SystemMessageId.YOU_CANNOT_MOVE_WHILE_SITTING);
			return false;
		}
		
		if (player.hasPet() || player.isMounted())
		{
			player.sendPacket(SystemMessageId.YOU_ALREADY_HAVE_A_PET);
			return false;
		}
		
		if (player.isAttackingNow())
		{
			player.sendPacket(SystemMessageId.YOU_CANNOT_SUMMON_DURING_COMBAT);
			return false;
		}
		
		final PetData petData = PetDataTable.getInstance().getPetDataByItemId(item.getId());
		if ((petData == null) || (petData.getNpcId() == -1))
		{
			return false;
		}
		
		player.addScript(new PetItemHolder(item));
		return super.useItem(playable, item, forceUse);
	}
}
