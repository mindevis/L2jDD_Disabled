
package org.l2jdd.commons.network;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.logging.Logger;

/**
 * @version $Revision: 1.2.4.1 $ $Date: 2005/03/27 15:30:11 $
 */
public abstract class BaseSendablePacket
{
	private static final Logger LOGGER = Logger.getLogger(BaseSendablePacket.class.getName());
	
	private final ByteArrayOutputStream _bao;
	
	protected BaseSendablePacket()
	{
		_bao = new ByteArrayOutputStream();
	}
	
	protected void writeD(int value)
	{
		_bao.write(value & 0xff);
		_bao.write((value >> 8) & 0xff);
		_bao.write((value >> 16) & 0xff);
		_bao.write((value >> 24) & 0xff);
	}
	
	protected void writeH(int value)
	{
		_bao.write(value & 0xff);
		_bao.write((value >> 8) & 0xff);
	}
	
	protected void writeC(int value)
	{
		_bao.write(value & 0xff);
	}
	
	protected void writeF(double org)
	{
		final long value = Double.doubleToRawLongBits(org);
		_bao.write((int) (value & 0xff));
		_bao.write((int) ((value >> 8) & 0xff));
		_bao.write((int) ((value >> 16) & 0xff));
		_bao.write((int) ((value >> 24) & 0xff));
		_bao.write((int) ((value >> 32) & 0xff));
		_bao.write((int) ((value >> 40) & 0xff));
		_bao.write((int) ((value >> 48) & 0xff));
		_bao.write((int) ((value >> 56) & 0xff));
	}
	
	protected void writeS(String text)
	{
		try
		{
			if (text != null)
			{
				_bao.write(text.getBytes(StandardCharsets.UTF_16LE));
			}
		}
		catch (Exception e)
		{
			LOGGER.warning(getClass().getSimpleName() + ": " + e.getMessage());
		}
		
		_bao.write(0);
		_bao.write(0);
	}
	
	protected void writeB(byte[] array)
	{
		try
		{
			_bao.write(array);
		}
		catch (IOException e)
		{
			LOGGER.warning(getClass().getSimpleName() + ": " + e.getMessage());
		}
	}
	
	protected void writeQ(long value)
	{
		_bao.write((int) (value & 0xff));
		_bao.write((int) ((value >> 8) & 0xff));
		_bao.write((int) ((value >> 16) & 0xff));
		_bao.write((int) ((value >> 24) & 0xff));
		_bao.write((int) ((value >> 32) & 0xff));
		_bao.write((int) ((value >> 40) & 0xff));
		_bao.write((int) ((value >> 48) & 0xff));
		_bao.write((int) ((value >> 56) & 0xff));
	}
	
	public int getLength()
	{
		return _bao.size() + 2;
	}
	
	public byte[] getBytes()
	{
		// if (this instanceof Init)
		// writeD(0x00); // reserve for XOR initial key
		
		writeD(0x00); // reserve for checksum
		
		final int padding = _bao.size() % 8;
		if (padding != 0)
		{
			for (int i = padding; i < 8; i++)
			{
				writeC(0x00);
			}
		}
		
		return _bao.toByteArray();
	}
	
	public abstract byte[] getContent() throws IOException;
}
