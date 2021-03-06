
package events.LoveYourGatekeeper;

import org.l2jdd.commons.util.Chronos;
import org.l2jdd.gameserver.model.actor.Npc;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.model.holders.SkillHolder;
import org.l2jdd.gameserver.model.itemcontainer.Inventory;
import org.l2jdd.gameserver.model.quest.LongTimeEvent;
import org.l2jdd.gameserver.network.SystemMessageId;
import org.l2jdd.gameserver.network.serverpackets.SystemMessage;

/**
 * Love Your Gatekeeper event.
 * @author Gladicek
 */
public class LoveYourGatekeeper extends LongTimeEvent
{
	// NPC
	private static final int GATEKEEPER = 32477;
	// Item
	private static final int GATEKEEPER_TRANSFORMATION_STICK = 12814;
	// Skills
	private static SkillHolder TELEPORTER_TRANSFORM = new SkillHolder(5655, 1);
	// Misc
	private static final int HOURS = 24;
	private static final int PRICE = 10000;
	private static final String REUSE = LoveYourGatekeeper.class.getSimpleName() + "_reuse";
	
	private LoveYourGatekeeper()
	{
		addStartNpc(GATEKEEPER);
		addFirstTalkId(GATEKEEPER);
		addTalkId(GATEKEEPER);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, PlayerInstance player)
	{
		switch (event)
		{
			case "transform_stick":
			{
				if (player.getAdena() >= PRICE)
				{
					final long reuse = player.getVariables().getLong(REUSE, 0);
					if (reuse > Chronos.currentTimeMillis())
					{
						final long remainingTime = (reuse - Chronos.currentTimeMillis()) / 1000;
						final int hours = (int) (remainingTime / 3600);
						final int minutes = (int) ((remainingTime % 3600) / 60);
						final SystemMessage sm = new SystemMessage(SystemMessageId.S1_WILL_BE_AVAILABLE_FOR_RE_USE_AFTER_S2_HOUR_S_S3_MINUTE_S);
						sm.addItemName(GATEKEEPER_TRANSFORMATION_STICK);
						sm.addInt(hours);
						sm.addInt(minutes);
						player.sendPacket(sm);
					}
					else
					{
						takeItems(player, Inventory.ADENA_ID, PRICE);
						giveItems(player, GATEKEEPER_TRANSFORMATION_STICK, 1);
						player.getVariables().set(REUSE, Chronos.currentTimeMillis() + (HOURS * 3600000));
					}
				}
				else
				{
					return "32477-3.htm";
				}
				return null;
			}
			case "transform":
			{
				if (!player.isTransformed())
				{
					player.doCast(TELEPORTER_TRANSFORM.getSkill());
				}
				return null;
			}
		}
		return event;
	}
	
	@Override
	public String onFirstTalk(Npc npc, PlayerInstance player)
	{
		return "32477.htm";
	}
	
	public static void main(String[] args)
	{
		new LoveYourGatekeeper();
	}
}
