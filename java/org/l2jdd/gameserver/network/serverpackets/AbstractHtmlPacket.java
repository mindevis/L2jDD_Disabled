
package org.l2jdd.gameserver.network.serverpackets;

import java.util.logging.Level;

import org.l2jdd.gameserver.cache.HtmCache;
import org.l2jdd.gameserver.enums.HtmlActionScope;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.util.Util;

/**
 * @author HorridoJoho
 */
public abstract class AbstractHtmlPacket implements IClientOutgoingPacket
{
	public static final char VAR_PARAM_START_CHAR = '$';
	
	private final int _npcObjId;
	private String _html = null;
	private boolean _disabledValidation = false;
	
	protected AbstractHtmlPacket()
	{
		_npcObjId = 0;
	}
	
	protected AbstractHtmlPacket(int npcObjId)
	{
		if (npcObjId < 0)
		{
			throw new IllegalArgumentException();
		}
		
		_npcObjId = npcObjId;
	}
	
	protected AbstractHtmlPacket(String html)
	{
		_npcObjId = 0;
		setHtml(html);
	}
	
	protected AbstractHtmlPacket(int npcObjId, String html)
	{
		if (npcObjId < 0)
		{
			throw new IllegalArgumentException();
		}
		
		_npcObjId = npcObjId;
		setHtml(html);
	}
	
	public void disableValidation()
	{
		_disabledValidation = true;
	}
	
	public void setHtml(String html)
	{
		if (html.length() > 17200)
		{
			LOGGER.log(Level.WARNING, "Html is too long! this will crash the client!", new Throwable());
			_html = html.substring(0, 17200);
		}
		
		if (!html.contains("<html") && !html.startsWith("..\\L2"))
		{
			_html = "<html><body>" + html + "</body></html>";
			return;
		}
		
		_html = html;
	}
	
	public boolean setFile(PlayerInstance player, String path)
	{
		final String content = HtmCache.getInstance().getHtm(player, path);
		if (content == null)
		{
			setHtml("<html><body>My Text is missing:<br>" + path + "</body></html>");
			LOGGER.warning("missing html page " + path);
			return false;
		}
		
		setHtml(content);
		return true;
	}
	
	public void replace(String pattern, String value)
	{
		_html = _html.replaceAll(pattern, value.replaceAll("\\$", "\\\\\\$"));
	}
	
	public void replace(String pattern, CharSequence value)
	{
		replace(pattern, String.valueOf(value));
	}
	
	public void replace(String pattern, boolean value)
	{
		replace(pattern, String.valueOf(value));
	}
	
	public void replace(String pattern, int value)
	{
		replace(pattern, String.valueOf(value));
	}
	
	public void replace(String pattern, long value)
	{
		replace(pattern, String.valueOf(value));
	}
	
	public void replace(String pattern, double value)
	{
		replace(pattern, String.valueOf(value));
	}
	
	@Override
	public void runImpl(PlayerInstance player)
	{
		if (player != null)
		{
			player.clearHtmlActions(getScope());
		}
		
		if (_disabledValidation)
		{
			return;
		}
		
		if (player != null)
		{
			Util.buildHtmlActionCache(player, getScope(), _npcObjId, _html);
		}
	}
	
	public int getNpcObjId()
	{
		return _npcObjId;
	}
	
	public String getHtml()
	{
		return _html;
	}
	
	public abstract HtmlActionScope getScope();
}
