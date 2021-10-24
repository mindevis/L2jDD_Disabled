
package org.l2jdd.tools.dbinstaller.util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @author mrTJO
 */
public class FileWriterStdout extends BufferedWriter
{
	public FileWriterStdout(FileWriter fileWriter)
	{
		super(fileWriter);
	}
	
	public void println() throws IOException
	{
		append(System.getProperty("line.separator"));
	}
	
	public void println(String line) throws IOException
	{
		append(line + System.getProperty("line.separator"));
	}
	
	public void print(String text) throws IOException
	{
		append(text);
	}
}
