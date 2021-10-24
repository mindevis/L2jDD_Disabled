
package handlers.effecthandlers;

import org.l2jdd.gameserver.data.xml.SkillData;
import org.l2jdd.gameserver.enums.SubclassInfoType;
import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.actor.Creature;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.model.effects.AbstractEffect;
import org.l2jdd.gameserver.model.items.instance.ItemInstance;
import org.l2jdd.gameserver.model.olympiad.OlympiadManager;
import org.l2jdd.gameserver.model.skills.Skill;
import org.l2jdd.gameserver.network.SystemMessageId;
import org.l2jdd.gameserver.network.serverpackets.AcquireSkillList;
import org.l2jdd.gameserver.network.serverpackets.ExStorageMaxCount;
import org.l2jdd.gameserver.network.serverpackets.ExSubjobInfo;
import org.l2jdd.gameserver.network.serverpackets.PartySmallWindowAll;
import org.l2jdd.gameserver.network.serverpackets.PartySmallWindowDeleteAll;
import org.l2jdd.gameserver.network.serverpackets.SystemMessage;
import org.l2jdd.gameserver.network.serverpackets.ability.ExAcquireAPSkillList;

/**
 * @author Sdw
 */
public class ClassChange extends AbstractEffect
{
	private final int _index;
	private static final int IDENTITY_CRISIS_SKILL_ID = 1570;
	
	public ClassChange(StatSet params)
	{
		_index = params.getInt("index", 0);
	}
	
	@Override
	public boolean isInstant()
	{
		return true;
	}
	
	@Override
	public void instant(Creature effector, Creature effected, Skill skill, ItemInstance item)
	{
		if (!effected.isPlayer())
		{
			return;
		}
		
		final PlayerInstance player = effected.getActingPlayer();
		if (player.isTransformed() || player.isSubclassLocked() || player.isAffectedBySkill(IDENTITY_CRISIS_SKILL_ID))
		{
			player.sendMessage("You cannot switch your class right now!");
			return;
		}
		
		final Skill identityCrisis = SkillData.getInstance().getSkill(IDENTITY_CRISIS_SKILL_ID, 1);
		if (identityCrisis != null)
		{
			identityCrisis.applyEffects(player, player);
		}
		
		if (OlympiadManager.getInstance().isRegisteredInComp(player))
		{
			OlympiadManager.getInstance().unRegisterNoble(player);
		}
		
		final int activeClass = player.getClassId().getId();
		player.setActiveClass(_index);
		
		final SystemMessage msg = new SystemMessage(SystemMessageId.YOU_HAVE_SUCCESSFULLY_SWITCHED_S1_TO_S2);
		msg.addClassId(activeClass);
		msg.addClassId(player.getClassId().getId());
		player.sendPacket(msg);
		
		player.updateSymbolSealSkills();
		player.broadcastUserInfo();
		player.sendPacket(new ExStorageMaxCount(player));
		player.sendPacket(new AcquireSkillList(player));
		player.sendPacket(new ExSubjobInfo(player, SubclassInfoType.CLASS_CHANGED));
		player.sendPacket(new ExAcquireAPSkillList(player));
		
		if (player.isInParty())
		{
			// Delete party window for other party members
			player.getParty().broadcastToPartyMembers(player, PartySmallWindowDeleteAll.STATIC_PACKET);
			for (PlayerInstance member : player.getParty().getMembers())
			{
				// And re-add
				if (member != player)
				{
					member.sendPacket(new PartySmallWindowAll(member, player.getParty()));
				}
			}
		}
	}
}
