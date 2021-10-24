
package org.l2jdd.gameserver.network.serverpackets;

import org.l2jdd.Config;
import org.l2jdd.commons.network.PacketWriter;
import org.l2jdd.gameserver.data.sql.CrestTable;
import org.l2jdd.gameserver.model.Crest;
import org.l2jdd.gameserver.network.OutgoingPackets;

public class PledgeCrest implements IClientOutgoingPacket
{
	private final int _crestId;
	private final byte[] _data;
	
	public PledgeCrest(int crestId)
	{
		_crestId = crestId;
		final Crest crest = CrestTable.getInstance().getCrest(crestId);
		_data = crest != null ? crest.getData() : null;
	}
	
	public PledgeCrest(int crestId, byte[] data)
	{
		_crestId = crestId;
		_data = data;
	}
	
	@Override
	public boolean write(PacketWriter packet)
	{
		OutgoingPackets.PLEDGE_CREST.writeId(packet);
		
		packet.writeD(Config.SERVER_ID);
		packet.writeD(_crestId);
		if (_data != null)
		{
			packet.writeD(_data.length);
			packet.writeB(_data);
		}
		else
		{
			packet.writeD(0);
		}
		return true;
	}
}
