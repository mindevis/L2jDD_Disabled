
package handlers.playeractions;

import org.l2jdd.gameserver.data.xml.PetDataTable;
import org.l2jdd.gameserver.data.xml.SkillData;
import org.l2jdd.gameserver.handler.IPlayerActionHandler;
import org.l2jdd.gameserver.model.ActionDataHolder;
import org.l2jdd.gameserver.model.actor.instance.PetInstance;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.model.skills.CommonSkill;
import org.l2jdd.gameserver.network.SystemMessageId;

/**
 * Pet skill use player action handler.
 * @author Nik
 */
public class PetSkillUse implements IPlayerActionHandler
{
	@Override
	public void useAction(PlayerInstance player, ActionDataHolder data, boolean ctrlPressed, boolean shiftPressed)
	{
		if (player.getTarget() == null)
		{
			return;
		}
		
		final PetInstance pet = player.getPet();
		if (pet == null)
		{
			player.sendPacket(SystemMessageId.YOU_DO_NOT_HAVE_A_PET);
		}
		else if (pet.isUncontrollable())
		{
			player.sendPacket(SystemMessageId.WHEN_YOUR_PET_S_HUNGER_GAUGE_IS_AT_0_YOU_CANNOT_USE_YOUR_PET);
		}
		else if (pet.isBetrayed())
		{
			player.sendPacket(SystemMessageId.YOUR_PET_SERVITOR_IS_UNRESPONSIVE_AND_WILL_NOT_OBEY_ANY_ORDERS);
		}
		else if ((pet.getLevel() - player.getLevel()) > 20)
		{
			player.sendPacket(SystemMessageId.YOUR_PET_IS_TOO_HIGH_LEVEL_TO_CONTROL);
		}
		else
		{
			final int skillLevel = PetDataTable.getInstance().getPetData(pet.getId()).getAvailableLevel(data.getOptionId(), pet.getLevel());
			if (skillLevel > 0)
			{
				pet.setTarget(player.getTarget());
				pet.useMagic(SkillData.getInstance().getSkill(data.getOptionId(), skillLevel), null, ctrlPressed, shiftPressed);
			}
			
			if (data.getOptionId() == CommonSkill.PET_SWITCH_STANCE.getId())
			{
				pet.switchMode();
			}
		}
	}
}
