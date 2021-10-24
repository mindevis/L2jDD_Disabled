
package org.l2jdd.gameserver.network.serverpackets;

import org.l2jdd.commons.network.PacketWriter;
import org.l2jdd.gameserver.enums.HtmlActionScope;
import org.l2jdd.gameserver.network.OutgoingPackets;

/**
 * @author JoeAlisson
 */
public class ExPremiumManagerShowHtml extends AbstractHtmlPacket
{
	public ExPremiumManagerShowHtml(String html)
	{
		super(html);
	}
	
	@Override
	public boolean write(PacketWriter packet)
	{
		OutgoingPackets.EX_PREMIUM_MANAGER_SHOW_HTML.writeId(packet);
		packet.writeD(getNpcObjId());
		packet.writeS(getHtml());
		packet.writeD(-1);
		packet.writeD(0);
		return true;
	}
	
	@Override
	public HtmlActionScope getScope()
	{
		return HtmlActionScope.PREMIUM_HTML;
	}
}
