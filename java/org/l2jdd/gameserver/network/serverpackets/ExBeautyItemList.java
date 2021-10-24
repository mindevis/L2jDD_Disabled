
package org.l2jdd.gameserver.network.serverpackets;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.l2jdd.commons.network.PacketWriter;
import org.l2jdd.gameserver.data.xml.BeautyShopData;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.model.beautyshop.BeautyData;
import org.l2jdd.gameserver.model.beautyshop.BeautyItem;
import org.l2jdd.gameserver.network.OutgoingPackets;

/**
 * @author Sdw
 */
public class ExBeautyItemList implements IClientOutgoingPacket
{
	private int _colorCount;
	private final BeautyData _beautyData;
	private final Map<Integer, List<BeautyItem>> _colorData = new HashMap<>();
	private static final int HAIR_TYPE = 0;
	private static final int FACE_TYPE = 1;
	private static final int COLOR_TYPE = 2;
	
	public ExBeautyItemList(PlayerInstance player)
	{
		_beautyData = BeautyShopData.getInstance().getBeautyData(player.getRace(), player.getAppearance().getSexType());
		for (BeautyItem hair : _beautyData.getHairList().values())
		{
			final List<BeautyItem> colors = new ArrayList<>();
			for (BeautyItem color : hair.getColors().values())
			{
				colors.add(color);
				_colorCount++;
			}
			_colorData.put(hair.getId(), colors);
		}
	}
	
	@Override
	public boolean write(PacketWriter packet)
	{
		OutgoingPackets.EX_BEAUTY_ITEM_LIST.writeId(packet);
		
		packet.writeD(HAIR_TYPE);
		packet.writeD(_beautyData.getHairList().size());
		for (BeautyItem hair : _beautyData.getHairList().values())
		{
			packet.writeD(0); // ?
			packet.writeD(hair.getId());
			packet.writeD(hair.getAdena());
			packet.writeD(hair.getResetAdena());
			packet.writeD(hair.getBeautyShopTicket());
			packet.writeD(1); // Limit
		}
		
		packet.writeD(FACE_TYPE);
		packet.writeD(_beautyData.getFaceList().size());
		for (BeautyItem face : _beautyData.getFaceList().values())
		{
			packet.writeD(0); // ?
			packet.writeD(face.getId());
			packet.writeD(face.getAdena());
			packet.writeD(face.getResetAdena());
			packet.writeD(face.getBeautyShopTicket());
			packet.writeD(1); // Limit
		}
		
		packet.writeD(COLOR_TYPE);
		packet.writeD(_colorCount);
		for (Entry<Integer, List<BeautyItem>> entry : _colorData.entrySet())
		{
			for (BeautyItem color : entry.getValue())
			{
				packet.writeD(entry.getKey());
				packet.writeD(color.getId());
				packet.writeD(color.getAdena());
				packet.writeD(color.getResetAdena());
				packet.writeD(color.getBeautyShopTicket());
				packet.writeD(1);
			}
		}
		return true;
	}
}
