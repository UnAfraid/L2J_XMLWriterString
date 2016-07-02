/*
 * Copyright (C) 2004-2014 L2J Server
 * 
 * This file is part of L2J Server.
 * 
 * L2J Server is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * L2J Server is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.l2jserver.xml;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.List;

/**
 * @author UnAfraid
 */
public class StringXmlWriterImpl extends AbstractXMLWriter
{
	private static final String TAB = "\t";
	private static final String EOL = "\r\n";
	
	@Override
	public void processDocument(File dest, XMLDocument xDoc) throws Exception
	{
		final StringBuilder sb = new StringBuilder();
		
		// Build the document
		processElements(sb, xDoc.getEntries(), 0);
		
		Files.write(dest.toPath(), sb.toString().getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE);
	}
	
	public void processElements(StringBuilder sb, List<XMLElement> entries, int tabIndex)
	{
		for (XMLElement entry : entries)
		{
			final int origIndex = tabIndex;
			sb.append(StringRepeat(TAB, tabIndex));
			sb.append("<" + entry.getName());
			
			// Create attributes
			for (XMLAttribute attr : entry.getAttributes())
			{
				sb.append(" " + attr.getName() + "=\"" + attr.getValue() + "\"");
			}
			
			// Create text if there is
			if (entry.getValue() != null)
			{
				sb.append(">" + entry.getValue() + "</" + entry.getName() + ">" + EOL);
			}
			else
			{
				if (entry.getEntries().isEmpty())
				{
					sb.append(" />" + EOL);
				}
				else
				{
					sb.append(">" + EOL);
				}
			}
			
			// Process other elements
			processElements(sb, entry.getEntries(), tabIndex + 1);
			
			// Close element
			if (!entry.getEntries().isEmpty())
			{
				sb.append(StringRepeat(TAB, origIndex));
				sb.append("</" + entry.getName() + ">" + EOL);
			}
		}
	}
	
	private static final String StringRepeat(String string, int times)
	{
		final StringBuilder sb = new StringBuilder();
		for (int i = 0; i < times; i++)
		{
			sb.append(string);
		}
		return sb.toString();
	}
}
