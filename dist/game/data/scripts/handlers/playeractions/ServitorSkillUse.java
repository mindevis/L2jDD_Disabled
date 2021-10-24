
package handlers.playeractions;

import org.l2jdd.gameserver.data.xml.PetSkillData;
import org.l2jdd.gameserver.data.xml.SkillData;
import org.l2jdd.gameserver.handler.IPlayerActionHandler;
import org.l2jdd.gameserver.model.ActionDataHolder;
import org.l2jdd.gameserver.model.actor.Summon;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.network.SystemMessageId;

/**
 * Summon skill use player action handler.
 * @author Nik
 */
public class ServitorSkillUse implements IPlayerActionHandler
{
	@Override
	public void useAction(PlayerInstance player, ActionDataHolder data, boolean ctrlPressed, boolean shiftPressed)
	{
		final Summon summon = player.getAnyServitor();
		if ((summon == null) || !summon.isServitor())
		{
			player.sendPacket(SystemMessageId.YOU_DO_NOT_HAVE_A_SERVITOR);
			return;
		}
		
		player.getServitors().values().forEach(servitor ->
		{
			if (summon.isBetrayed())
			{
				player.sendPacket(SystemMessageId.YOUR_PET_SERVITOR_IS_UNRESPONSIVE_AND_WILL_NOT_OBEY_ANY_ORDERS);
				return;
			}
			
			final int skillLevel = PetSkillData.getInstance().getAvailableLevel(servitor, data.getOptionId());
			if (skillLevel > 0)
			{
				servitor.setTarget(player.getTarget());
				servitor.useMagic(SkillData.getInstance().getSkill(data.getOptionId(), skillLevel), null, ctrlPressed, shiftPressed);
			}
		});
	}
}
