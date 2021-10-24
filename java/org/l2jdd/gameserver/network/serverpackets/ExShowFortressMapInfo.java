
package org.l2jdd.gameserver.network.serverpackets;

import java.util.List;

import org.l2jdd.commons.network.PacketWriter;
import org.l2jdd.gameserver.instancemanager.FortSiegeManager;
import org.l2jdd.gameserver.model.FortSiegeSpawn;
import org.l2jdd.gameserver.model.Spawn;
import org.l2jdd.gameserver.model.siege.Fort;
import org.l2jdd.gameserver.network.OutgoingPackets;

/**
 * TODO: Rewrite!!!!!!
 * @author KenM
 */
public class ExShowFortressMapInfo implements IClientOutgoingPacket
{
	private final Fort _fortress;
	
	public ExShowFortressMapInfo(Fort fortress)
	{
		_fortress = fortress;
	}
	
	@Override
	public boolean write(PacketWriter packet)
	{
		OutgoingPackets.EX_SHOW_FORTRESS_MAP_INFO.writeId(packet);
		
		packet.writeD(_fortress.getResidenceId());
		packet.writeD(_fortress.getSiege().isInProgress() ? 1 : 0); // fortress siege status
		packet.writeD(_fortress.getFortSize()); // barracks count
		
		final List<FortSiegeSpawn> commanders = FortSiegeManager.getInstance().getCommanderSpawnList(_fortress.getResidenceId());
		if ((commanders != null) && !commanders.isEmpty() && _fortress.getSiege().isInProgress())
		{
			switch (commanders.size())
			{
				case 3:
				{
					for (FortSiegeSpawn spawn : commanders)
					{
						if (isSpawned(spawn.getId()))
						{
							packet.writeD(0);
						}
						else
						{
							packet.writeD(1);
						}
					}
					break;
				}
				case 4: // TODO: change 4 to 5 once control room supported
				{
					int count = 0;
					for (FortSiegeSpawn spawn : commanders)
					{
						count++;
						if (count == 4)
						{
							packet.writeD(1); // TODO: control room emulated
						}
						if (isSpawned(spawn.getId()))
						{
							packet.writeD(0);
						}
						else
						{
							packet.writeD(1);
						}
					}
					break;
				}
			}
		}
		else
		{
			for (int i = 0; i < _fortress.getFortSize(); i++)
			{
				packet.writeD(0);
			}
		}
		return true;
	}
	
	/**
	 * @param npcId
	 * @return
	 */
	private boolean isSpawned(int npcId)
	{
		boolean ret = false;
		for (Spawn spawn : _fortress.getSiege().getCommanders())
		{
			if (spawn.getId() == npcId)
			{
				ret = true;
				break;
			}
		}
		return ret;
	}
}
