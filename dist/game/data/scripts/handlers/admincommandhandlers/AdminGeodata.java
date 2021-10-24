
package handlers.admincommandhandlers;

import java.util.StringTokenizer;

import org.l2jdd.gameserver.geoengine.GeoEngine;
import org.l2jdd.gameserver.handler.IAdminCommandHandler;
import org.l2jdd.gameserver.model.World;
import org.l2jdd.gameserver.model.WorldObject;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.network.SystemMessageId;
import org.l2jdd.gameserver.network.serverpackets.SystemMessage;
import org.l2jdd.gameserver.util.BuilderUtil;
import org.l2jdd.gameserver.util.GeoUtils;

/**
 * @author -Nemesiss-, HorridoJoho
 */
public class AdminGeodata implements IAdminCommandHandler
{
	private static final String[] ADMIN_COMMANDS =
	{
		"admin_geo_pos",
		"admin_geo_spawn_pos",
		"admin_geo_can_move",
		"admin_geo_can_see",
		"admin_geogrid",
		"admin_geomap"
	};
	
	@Override
	public boolean useAdminCommand(String command, PlayerInstance activeChar)
	{
		final StringTokenizer st = new StringTokenizer(command, " ");
		final String actualCommand = st.nextToken();
		switch (actualCommand.toLowerCase())
		{
			case "admin_geo_pos":
			{
				final int worldX = activeChar.getX();
				final int worldY = activeChar.getY();
				final int worldZ = activeChar.getZ();
				final int geoX = GeoEngine.getGeoX(worldX);
				final int geoY = GeoEngine.getGeoY(worldY);
				
				if (GeoEngine.getInstance().hasGeoPos(geoX, geoY))
				{
					BuilderUtil.sendSysMessage(activeChar, "WorldX: " + worldX + ", WorldY: " + worldY + ", WorldZ: " + worldZ + ", GeoX: " + geoX + ", GeoY: " + geoY + ", GeoZ: " + GeoEngine.getInstance().getHeight(worldX, worldY, worldZ));
				}
				else
				{
					BuilderUtil.sendSysMessage(activeChar, "There is no geodata at this position.");
				}
				break;
			}
			case "admin_geo_spawn_pos":
			{
				final int worldX = activeChar.getX();
				final int worldY = activeChar.getY();
				final int worldZ = activeChar.getZ();
				final int geoX = GeoEngine.getGeoX(worldX);
				final int geoY = GeoEngine.getGeoY(worldY);
				
				if (GeoEngine.getInstance().hasGeoPos(geoX, geoY))
				{
					BuilderUtil.sendSysMessage(activeChar, "WorldX: " + worldX + ", WorldY: " + worldY + ", WorldZ: " + worldZ + ", GeoX: " + geoX + ", GeoY: " + geoY + ", GeoZ: " + GeoEngine.getInstance().getHeight(worldX, worldY, worldZ));
				}
				else
				{
					BuilderUtil.sendSysMessage(activeChar, "There is no geodata at this position.");
				}
				break;
			}
			case "admin_geo_can_move":
			{
				final WorldObject target = activeChar.getTarget();
				if (target != null)
				{
					if (GeoEngine.getInstance().canSeeTarget(activeChar, target))
					{
						BuilderUtil.sendSysMessage(activeChar, "Can move beeline.");
					}
					else
					{
						BuilderUtil.sendSysMessage(activeChar, "Can not move beeline!");
					}
				}
				else
				{
					activeChar.sendPacket(SystemMessageId.INVALID_TARGET);
				}
				break;
			}
			case "admin_geo_can_see":
			{
				final WorldObject target = activeChar.getTarget();
				if (target != null)
				{
					if (GeoEngine.getInstance().canSeeTarget(activeChar, target))
					{
						BuilderUtil.sendSysMessage(activeChar, "Can see target.");
					}
					else
					{
						activeChar.sendPacket(new SystemMessage(SystemMessageId.CANNOT_SEE_TARGET));
					}
				}
				else
				{
					activeChar.sendPacket(SystemMessageId.INVALID_TARGET);
				}
				break;
			}
			case "admin_geogrid":
			{
				GeoUtils.debugGrid(activeChar);
				break;
			}
			case "admin_geomap":
			{
				final int x = ((activeChar.getX() - World.WORLD_X_MIN) >> 15) + World.TILE_X_MIN;
				final int y = ((activeChar.getY() - World.WORLD_Y_MIN) >> 15) + World.TILE_Y_MIN;
				BuilderUtil.sendSysMessage(activeChar, "GeoMap: " + x + "_" + y + " (" + ((x - World.TILE_ZERO_COORD_X) * World.TILE_SIZE) + "," + ((y - World.TILE_ZERO_COORD_Y) * World.TILE_SIZE) + " to " + ((((x - World.TILE_ZERO_COORD_X) * World.TILE_SIZE) + World.TILE_SIZE) - 1) + "," + ((((y - World.TILE_ZERO_COORD_Y) * World.TILE_SIZE) + World.TILE_SIZE) - 1) + ")");
				break;
			}
		}
		return true;
	}
	
	@Override
	public String[] getAdminCommandList()
	{
		return ADMIN_COMMANDS;
	}
}
