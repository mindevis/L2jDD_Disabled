
package org.l2jdd.gameserver.network.serverpackets;

import java.util.Set;

import org.l2jdd.commons.network.PacketWriter;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.model.skills.AbnormalVisualEffect;
import org.l2jdd.gameserver.network.OutgoingPackets;

/**
 * @author Sdw
 */
public class ExUserInfoAbnormalVisualEffect implements IClientOutgoingPacket
{
	private final PlayerInstance _player;
	
	public ExUserInfoAbnormalVisualEffect(PlayerInstance player)
	{
		_player = player;
	}
	
	@Override
	public boolean write(PacketWriter packet)
	{
		OutgoingPackets.EX_USER_INFO_ABNORMAL_VISUAL_EFFECT.writeId(packet);
		
		packet.writeD(_player.getObjectId());
		packet.writeD(_player.getTransformationId());
		
		final Set<AbnormalVisualEffect> abnormalVisualEffects = _player.getEffectList().getCurrentAbnormalVisualEffects();
		final boolean isInvisible = _player.isInvisible();
		packet.writeD(abnormalVisualEffects.size() + (isInvisible ? 1 : 0));
		for (AbnormalVisualEffect abnormalVisualEffect : abnormalVisualEffects)
		{
			packet.writeH(abnormalVisualEffect.getClientId());
		}
		if (isInvisible)
		{
			packet.writeH(AbnormalVisualEffect.STEALTH.getClientId());
		}
		return true;
	}
}
